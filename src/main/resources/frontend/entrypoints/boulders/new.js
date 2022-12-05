import {createSVGNode} from '../../components/create_node'

init()

function isTouchDevice() {
	return (('ontouchstart' in window) ||
     (navigator.maxTouchPoints > 0) ||
     (navigator.msMaxTouchPoints > 0));
}


let isInit = false

function init() {
	if (isInit) return
	isInit = true
	console.log("init holds editor")
	
	const svg = document.querySelector('svg')
	const pan = svg.querySelector('#scene')
	const holdsClipPath = pan.querySelector("#holdsClipPath")
	const holdsInput = document.querySelector("#holdsInput")
	
	try {
		console.log(holdsInput.value)
		const savedHolds = JSON.parse(holdsInput.value)
		if (Array.isArray(savedHolds)) {
			savedHolds.forEach(p => drawHold(p))	
		}
	} catch (e) {
		console.log("no saved holds")
	}
	
	document.addEventListener(isTouchDevice() ? 'touchstart' : 'mousedown', handleTouchStart, false)
	
	let lastTouchStart
	function handleTouchStart(evt) {
		if (evt.touches && evt.touches.length > 1) return //do not handle multitouch events
	 	const time = new Date().getTime()
		if (!lastTouchStart || (time - lastTouchStart) > 200) {
			lastTouchStart = time
			console.log(evt.target)
		} else {
			createHold(evt)
		}	
	}
	
	function createHold(evt) {
		const pt = svg.createSVGPoint()
		pt.x = evt.clientX || evt.touches[0].clientX
		pt.y = evt.clientY || evt.touches[0].clientY
		
		const ptr = pt.matrixTransform(svg.getScreenCTM().inverse())
		
		requestAnimationFrame(() => drawHold(ptr))
	}
	
	function drawHold(ptr) {
		pan.appendChild(createSVGNode(`<use class="hold" xlink:href="#hold" x=${ptr.x} y=${ptr.y}></use>`))
		holdsClipPath.appendChild(createSVGNode(`<use xlink:href="#holdClipPath" x=${ptr.x} y=${ptr.y}></use>`))
		holdsInput.value = JSON.stringify(
			Array.from(
				pan.querySelectorAll('.hold'))
					.map(h => ({x: h.getAttribute('x'), y: h.getAttribute('y')})
			)
		)
	}
}

