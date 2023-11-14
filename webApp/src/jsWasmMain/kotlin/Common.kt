
//
//@Composable
//internal fun ImageViewerWeb() {
//    val toastState = remember { mutableStateOf<ToastState>(ToastState.Hidden) }
//    val ioScope: CoroutineScope = rememberCoroutineScope { ioDispatcher }
//    val dependencies = remember(ioScope) { getDependencies(ioScope, toastState) }
//
//    ImageViewerTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            ImageViewerCommon(
//                dependencies = dependencies
//            )
//            Toast(toastState)
//        }
//    }
//}
