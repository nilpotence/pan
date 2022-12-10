import {createSVGNode} from '../../components/create_node'


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
	document.addEventListener(isTouchDevice() ? 'touchmove' : 'mousemove', handleTouchMove, false)
	document.addEventListener(isTouchDevice() ? 'touchend' : 'mouseup', handleTouchEnd, false)
	
	function isHold(evt) {
		return evt.target.tagName == 'use' && evt.target.classList.contains('hold')
	}
	
	let lastTouchStart
	let dragged = null
	let dragTransformMatrix = null
	let draggedPt = null
	function handleTouchStart(evt) {
		if (evt.touches && evt.touches.length > 1) return //do not handle multitouch events
	 	const time = new Date().getTime()
		if (!lastTouchStart || (time - lastTouchStart) > 200) {
			lastTouchStart = time
			if (isHold(evt)) {
				dragStart(evt)		
			}
		} else {
			if (isHold(evt)) {
				deleteHold(evt)
			} else {
				createHold(evt)
			}
		}	
	}
	
	function handleTouchMove (evt) {
		if (dragged) drag(evt)
	}
	
	function handleTouchEnd (evt) {
		if (dragged) dragEnd(evt)
	}
	
	function dragStart(evt) {
		dragged = evt.target
		dragTransformMatrix = svg.getScreenCTM().inverse()
		draggedPt = svg.createSVGPoint()
	}
	
	function drag(evt) {
		requestAnimationFrame(() => {
			draggedPt.x = evt.clientX || evt.touches[0].clientX
			draggedPt.y = evt.clientY || evt.touches[0].clientY
			draggedPt = draggedPt.matrixTransform(dragTransformMatrix)
				
			dragged.setAttribute('x', draggedPt.x)	
			dragged.setAttribute('y', draggedPt.y)	
			dragged.__clipPath.setAttribute('x', draggedPt.x)
			dragged.__clipPath.setAttribute('y', draggedPt.y)
		})	
	}
	
	function dragEnd() {
		dragged = null	
		dragTransformMatrix = null
		draggedPt = null

		updateInput()
	}
	
	function deleteHold(evt) {
		requestAnimationFrame(() => {
			pan.removeChild(evt.target)
			holdsClipPath.removeChild(evt.target.__clipPath)
		
			updateInput()
		})
	}
	
	function createHold(evt) {
		const pt = svg.createSVGPoint()
		pt.x = evt.clientX || evt.touches[0].clientX
		pt.y = evt.clientY || evt.touches[0].clientY
		
		const ptr = pt.matrixTransform(svg.getScreenCTM().inverse())
		
		requestAnimationFrame(() => drawHold(ptr))
	}
	
	function drawHold(ptr) {
		const newHold = createSVGNode(`<use class="hold" xlink:href="#hold" x=${ptr.x} y=${ptr.y}></use>`)
		const newHoldClipPath  = createSVGNode(`<use xlink:href="#holdClipPath" x=${ptr.x} y=${ptr.y}></use>`)
		newHold.__clipPath = newHoldClipPath
		
		pan.appendChild(newHold)
		holdsClipPath.appendChild(newHoldClipPath)

		updateInput()
	}
	
	function updateInput() {
		holdsInput.value = JSON.stringify(
			Array.from(
				pan.querySelectorAll('.hold'))
					.map(h => ({x: h.getAttribute('x'), y: h.getAttribute('y')})
			)
		)
	}
}


init()