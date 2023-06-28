package sharedelementtransaction

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.toSize
import sharedelementtransaction.SharedElementTransition.InProgress
import sharedelementtransaction.SharedElementTransition.WaitingForEndElementPosition
import sharedelementtransaction.SharedElementsTracker.State.Empty
import sharedelementtransaction.SharedElementsTracker.State.EndElementRegistered
import sharedelementtransaction.SharedElementsTracker.State.InTransition
import sharedelementtransaction.SharedElementsTracker.State.StartElementPositioned
import sharedelementtransaction.SharedElementsTracker.State.StartElementRegistered
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Composable
internal fun BaseSharedElement(
    elementInfo: SharedElementInfo,
    isFullscreen: Boolean,
    placeholder: @Composable () -> Unit,
    overlay: @Composable (SharedElementsTransitionState) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val (savedShouldHide, setShouldHide) = remember { mutableStateOf(false) }
    val rootState = LocalSharedElementsRootState.current
    val shouldHide = rootState.onElementRegistered(elementInfo)
    setShouldHide(shouldHide)

    val compositionLocalContext = currentCompositionLocalContext
    if (isFullscreen) {
        rootState.onElementPositioned(
            elementInfo,
            compositionLocalContext,
            placeholder,
            overlay,
            null,
            setShouldHide
        )

        Spacer(modifier = Modifier.fillMaxSize())
    } else {
        val contentModifier = Modifier.onGloballyPositioned { coordinates ->
            rootState.onElementPositioned(
                elementInfo,
                compositionLocalContext,
                placeholder,
                overlay,
                coordinates,
                setShouldHide
            )
        }.run {
            if (shouldHide || savedShouldHide) alpha(0f) else this
        }

        content(contentModifier)
    }

    DisposableEffect(elementInfo) {
        onDispose {
            rootState.onElementDisposed(elementInfo)
        }
    }
}

@Composable
fun SharedElementsRoot(
    content: @Composable SharedElementsRootScope.() -> Unit
) {
    val rootState = remember { SharedElementsRootState() }

    Box(modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
        rootState.rootCoordinates = layoutCoordinates
        rootState.rootBounds = Rect(Offset.Zero, layoutCoordinates.size.toSize())
    }) {
        CompositionLocalProvider(
            LocalSharedElementsRootState provides rootState,
            LocalSharedElementsRootScope provides rootState.scope
        ) {
            rootState.scope.content()
            UnboundedBox { SharedElementTransitionsOverlay(rootState) }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            rootState.onDispose()
        }
    }
}

interface SharedElementsRootScope {
    val isRunningTransition: Boolean
    fun prepareTransition(vararg elements: Any)
}

val LocalSharedElementsRootScope = staticCompositionLocalOf<SharedElementsRootScope?> { null }

@Composable
private fun UnboundedBox(content: @Composable () -> Unit) {
    Layout(content) { measurables, constraints ->
        val infiniteConstraints = Constraints()
        val placeables = measurables.fastMap {
            val isFullscreen = it.layoutId === FullscreenLayoutId
            it.measure(if (isFullscreen) constraints else infiniteConstraints)
        }
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.fastForEach { it.place(0, 0) }
        }
    }
}

@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
inline fun <T, R> List<T>.fastMap(transform: (T) -> R): List<R> {
    contract { callsInPlace(transform) }
    val target = ArrayList<R>(size)
    fastForEach {
        target += transform(it)
    }
    return target
}

@Suppress("BanInlineOptIn")
@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    contract { callsInPlace(action) }
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

@Composable
private fun SharedElementTransitionsOverlay(rootState: SharedElementsRootState) {
    rootState.recomposeScope = currentRecomposeScope
    rootState.trackers.forEach { (key, tracker) ->
        key(key) {
            val transition = tracker.transition
            val start = (tracker.state as? StartElementPositioned)?.startElement
            if (transition != null || (start != null && start.bounds == null)) {
                val startElement = start ?: transition!!.startElement
                val startScreenKey = startElement.info.screenKey
                val endElement = (transition as? InProgress)?.endElement
                val spec = startElement.info.spec
                val animated = remember(startScreenKey) { Animatable(0f) }
                val fraction = animated.value
                startElement.info.onFractionChanged?.invoke(fraction)
                endElement?.info?.onFractionChanged?.invoke(1 - fraction)

                val direction = if (endElement == null) null else remember(startScreenKey) {
                    val direction = spec.direction
                    if (direction != TransitionDirection.Auto) direction else
                        calculateDirection(
                            startElement.bounds ?: rootState.rootBounds!!,
                            endElement.bounds ?: rootState.rootBounds!!
                        )
                }

                startElement.Placeholder(
                    rootState, fraction, endElement,
                    direction, spec, tracker.pathMotion
                )

                if (transition is InProgress) {
                    LaunchedEffect(transition, animated) {
                        repeat(spec.waitForFrames) { withFrameNanos {} }
                        animated.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(
                                durationMillis = spec.durationMillis,
                                delayMillis = spec.delayMillis,
                                easing = spec.easing
                            )
                        )
                        transition.onTransitionFinished()
                    }
                }
            }
        }
    }
}

@Composable
private fun PositionedSharedElement.Placeholder(
    rootState: SharedElementsRootState,
    fraction: Float,
    end: PositionedSharedElement? = null,
    direction: TransitionDirection? = null,
    spec: SharedElementsTransitionSpec? = null,
    pathMotion: PathMotion? = null
) {
    overlay(
        SharedElementsTransitionState(
            fraction = fraction,
            startInfo = info,
            startBounds = if (end == null) bounds else bounds ?: rootState.rootBounds,
            startCompositionLocalContext = compositionLocalContext,
            startPlaceholder = placeholder,
            endInfo = end?.info,
            endBounds = end?.run { bounds ?: rootState.rootBounds },
            endCompositionLocalContext = end?.compositionLocalContext,
            endPlaceholder = end?.placeholder,
            direction = direction,
            spec = spec,
            pathMotion = pathMotion
        )
    )
}

private val LocalSharedElementsRootState = staticCompositionLocalOf<SharedElementsRootState> {
    error("SharedElementsRoot not found. SharedElement must be hosted in SharedElementsRoot.")
}

private class SharedElementsRootState {
    private val choreographer = ChoreographerWrapper()
    val scope: SharedElementsRootScope = Scope()
    var trackers by mutableStateOf(mapOf<Any, SharedElementsTracker>())
    var recomposeScope: RecomposeScope? = null
    var rootCoordinates: LayoutCoordinates? = null
    var rootBounds: Rect? = null

    fun onElementRegistered(elementInfo: SharedElementInfo): Boolean {
        choreographer.removeCallback(elementInfo)
        return getTracker(elementInfo).onElementRegistered(elementInfo)
    }

    fun onElementPositioned(
        elementInfo: SharedElementInfo,
        compositionLocalContext: CompositionLocalContext,
        placeholder: @Composable () -> Unit,
        overlay: @Composable (SharedElementsTransitionState) -> Unit,
        coordinates: LayoutCoordinates?,
        setShouldHide: (Boolean) -> Unit
    ) {
        val element = PositionedSharedElement(
            info = elementInfo,
            compositionLocalContext = compositionLocalContext,
            placeholder = placeholder,
            overlay = overlay,
            bounds = coordinates?.calculateBoundsInRoot()
        )
        getTracker(elementInfo).onElementPositioned(element, setShouldHide)
    }

    fun onElementDisposed(elementInfo: SharedElementInfo) {
        choreographer.postCallback(elementInfo) {
            val tracker = getTracker(elementInfo)
            tracker.onElementUnregistered(elementInfo)
            if (tracker.isEmpty) trackers = trackers - elementInfo.key
        }
    }

    fun onDispose() {
        choreographer.clear()
    }

    private fun getTracker(elementInfo: SharedElementInfo): SharedElementsTracker {
        return trackers[elementInfo.key] ?: SharedElementsTracker { transition ->
            recomposeScope?.invalidate()
            (scope as Scope).isRunningTransition = if (transition != null) true else
                trackers.values.any { it.transition != null }
        }.also { trackers = trackers + (elementInfo.key to it) }
    }

    private fun LayoutCoordinates.calculateBoundsInRoot(): Rect =
        Rect(
            rootCoordinates?.localPositionOf(this, Offset.Zero)
                ?: positionInRoot(), size.toSize()
        )

    private inner class Scope : SharedElementsRootScope {

        override var isRunningTransition: Boolean by mutableStateOf(false)

        override fun prepareTransition(vararg elements: Any) {
            elements.forEach {
                trackers[it]?.prepareTransition()
            }
        }

    }

}

private class SharedElementsTracker(
    private val onTransitionChanged: (SharedElementTransition?) -> Unit
) {
    var state: State = Empty

    var pathMotion: PathMotion? = null

    // Use snapshot state to trigger recomposition of start element when transition starts
    private var _transition: SharedElementTransition? by mutableStateOf(null)
    var transition: SharedElementTransition?
        get() = _transition
        set(value) {
            if (_transition != value) {
                _transition = value
                if (value == null) pathMotion = null
                onTransitionChanged(value)
            }
        }

    val isEmpty: Boolean get() = state is Empty

    private fun StartElementPositioned.prepareTransition() {
        if (transition !is WaitingForEndElementPosition) {
            transition = WaitingForEndElementPosition(startElement)
        }
    }

    fun prepareTransition() {
        (state as? StartElementPositioned)?.prepareTransition()
    }

    fun onElementRegistered(elementInfo: SharedElementInfo): Boolean {
        var shouldHide = false

        val transition = transition
        if (transition is InProgress
            && elementInfo != transition.startElement.info
            && elementInfo != transition.endElement.info
        ) {
            state = StartElementPositioned(startElement = transition.endElement)
            this.transition = null
        }

        when (val state = state) {
            is StartElementPositioned -> {
                if (!state.isRegistered(elementInfo)) {
                    shouldHide = true
                    this.state = EndElementRegistered(
                        startElement = state.startElement,
                        endElementInfo = elementInfo
                    )
                    state.prepareTransition()
                }
            }

            is StartElementRegistered -> {
                if (elementInfo != state.startElementInfo) {
                    this.state = StartElementRegistered(startElementInfo = elementInfo)
                }
            }

            is Empty -> {
                this.state = StartElementRegistered(startElementInfo = elementInfo)
            }

            else -> Unit
        }
        return shouldHide || transition != null
    }

    fun onElementPositioned(element: PositionedSharedElement, setShouldHide: (Boolean) -> Unit) {
        val state = state
        if (state is StartElementPositioned && element.info == state.startElementInfo) {
            state.startElement = element
            return
        }

        when (state) {
            is EndElementRegistered -> {
                if (element.info == state.endElementInfo) {
                    this.state = InTransition
                    val spec = element.info.spec
                    this.pathMotion = spec.pathMotionFactory()
                    transition = InProgress(
                        startElement = state.startElement,
                        endElement = element,
                        onTransitionFinished = {
                            this.state = StartElementPositioned(startElement = element)
                            transition = null
                            setShouldHide(false)
                        }
                    )
                }
            }

            is StartElementRegistered -> {
                if (element.info == state.startElementInfo) {
                    this.state = StartElementPositioned(startElement = element)
                }
            }

            else -> Unit
        }
    }

    fun onElementUnregistered(elementInfo: SharedElementInfo) {
        when (val state = state) {
            is EndElementRegistered -> {
                if (elementInfo == state.endElementInfo) {
                    this.state = StartElementPositioned(startElement = state.startElement)
                    transition = null
                } else if (elementInfo == state.startElement.info) {
                    this.state = StartElementRegistered(startElementInfo = state.endElementInfo)
                    transition = null
                }
            }

            is StartElementRegistered -> {
                if (elementInfo == state.startElementInfo) {
                    this.state = Empty
                    transition = null
                }
            }

            else -> Unit
        }
    }

    sealed class State {
        object Empty : State()

        open class StartElementRegistered(val startElementInfo: SharedElementInfo) : State() {
            open fun isRegistered(elementInfo: SharedElementInfo): Boolean {
                return elementInfo == startElementInfo
            }
        }

        open class StartElementPositioned(var startElement: PositionedSharedElement) :
            StartElementRegistered(startElement.info)

        class EndElementRegistered(
            startElement: PositionedSharedElement,
            val endElementInfo: SharedElementInfo
        ) : StartElementPositioned(startElement) {
            override fun isRegistered(elementInfo: SharedElementInfo): Boolean {
                return super.isRegistered(elementInfo) || elementInfo == endElementInfo
            }
        }

        object InTransition : State()
    }
}

enum class TransitionDirection {
    Auto, Enter, Return
}

enum class FadeMode {
    In, Out, Cross, Through
}

const val FadeThroughProgressThreshold = 0.35f

internal class SharedElementsTransitionState(
    val fraction: Float,
    val startInfo: SharedElementInfo,
    val startBounds: Rect?,
    val startCompositionLocalContext: CompositionLocalContext,
    val startPlaceholder: @Composable () -> Unit,
    val endInfo: SharedElementInfo?,
    val endBounds: Rect?,
    val endCompositionLocalContext: CompositionLocalContext?,
    val endPlaceholder: (@Composable () -> Unit)?,
    val direction: TransitionDirection?,
    val spec: SharedElementsTransitionSpec?,
    val pathMotion: PathMotion?
)

internal val TopLeft = TransformOrigin(0f, 0f)

open class SharedElementInfo(
    val key: Any,
    val screenKey: Any,
    val spec: SharedElementsTransitionSpec,
    val onFractionChanged: ((Float) -> Unit)?
) {

    final override fun equals(other: Any?): Boolean =
        other is SharedElementInfo && other.key == key && other.screenKey == screenKey

    final override fun hashCode(): Int = 31 * key.hashCode() + screenKey.hashCode()

}

private class PositionedSharedElement(
    val info: SharedElementInfo,
    val compositionLocalContext: CompositionLocalContext,
    val placeholder: @Composable () -> Unit,
    val overlay: @Composable (SharedElementsTransitionState) -> Unit,
    val bounds: Rect?
)

private sealed class SharedElementTransition(val startElement: PositionedSharedElement) {

    class WaitingForEndElementPosition(startElement: PositionedSharedElement) :
        SharedElementTransition(startElement)

    class InProgress(
        startElement: PositionedSharedElement,
        val endElement: PositionedSharedElement,
        val onTransitionFinished: () -> Unit
    ) : SharedElementTransition(startElement)

}

class ChoreographerWrapper {
    private val callbacks = mutableMapOf<SharedElementInfo, () -> Unit>()

    fun postCallback(elementInfo: SharedElementInfo, callback: () -> Unit) {
        if (callbacks.containsKey(elementInfo)) return

        val frameCallback = {
            callbacks.remove(elementInfo)
            callback()
        }
        callbacks[elementInfo] = frameCallback

//        val frameClock = MonotonicFrameClockAmbient.current
//        val disposeEffect = effectOf<Unit> {
//            onDispose {
//                frameClock.removeFrameCallback(frameCallback)
//            }
//        }
//        onCommit(frameCallback) {
//            frameClock.invokeOnCommit(frameCallback)
//            disposeEffect.invoke()
    }

    fun removeCallback(elementInfo: SharedElementInfo) {
//        callbacks.remove(elementInfo)?.let { frameCallback ->
//            val frameClock = MonotonicFrameClockAmbient.current
//            frameClock.removeFrameCallback(frameCallback)
//        }
    }

    fun clear() {
//        val frameClock = MonotonicFrameClockAmbient.current
//        callbacks.values.forEach { frameCallback ->
//            frameClock.removeFrameCallback(frameCallback)
//        }
//        callbacks.clear()
    }
}


internal val Fullscreen = Modifier.fillMaxSize()
internal val FullscreenLayoutId = Any()
