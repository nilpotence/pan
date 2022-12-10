import esbuild from 'esbuild';

const watch = process.argv.includes('--watch') ? 
	{
		onRebuild(error, result) {
			  if (error) console.error('watch build failed:', error)
			  else console.log('watch build succeeded:', result)
		}
	}
	:
	false

esbuild
	.build({
		entryPoints: [
			'src/main/resources/frontend/entrypoints/index.js',
			'src/main/resources/frontend/entrypoints/boulders/new.js',
			'src/main/resources/frontend/entrypoints/boulders/show.js'
		],
		outdir: 'target/classes/static/bundle/',
		loader: {
			'.woff2': 'file',
			'.woff': 'file',
			'.svg': 'file'
		},
		bundle: true,
		watch: watch,
		minify: true
	})
	.catch(() => process.exit(1))