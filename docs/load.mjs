import { instantiate } from './recipeapp.uninstantiated.mjs';

await wasmSetup;

instantiate({ skia: Module['asm'] });
