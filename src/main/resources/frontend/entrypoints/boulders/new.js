function isTouchDevice() {
	return (('ontouchstart' in window) ||
     (navigator.maxTouchPoints > 0) ||
     (navigator.msMaxTouchPoints > 0));
}

function initCanvas() {
	const canvas = document.querySelector("canvas")
	const ctx = canvas.getContext('2d')	
	const holdsInput = document.querySelector("#holdsInput")

	
	let holds = []
	
	try {
		holds = JSON.parse (holdsInput.value)
	} catch (e) {
		holds = []
	}

	canvas.addEventListener(isTouchDevice() ? 'touchstart' : 'mousedown', onTouchDown)	
	canvas.addEventListener(isTouchDevice() ? 'touchend' : 'mouseup', onTouchUp)	
	
	let lastTouchStart = 0
	function onTouchDown(evt) {
		if (evt.touches && evt.touches.length > 1) return //do not handle multitouch events
	 	const time = new Date().getTime()
		if (!lastTouchStart || (time - lastTouchStart) > 200) {
			lastTouchStart = time
		} else {
			evt.preventDefault()
			evt.stopPropagation()
			const pos = getPos(evt)
			const hold = getHold(pos)
			if (hold) {
				deleteHold(hold)
			} else {
				createHold(pos)
			}
		}	
	}
	
	function onTouchUp(evt) {
		
	}
	
	function getPos(evt) {
		const rect = canvas.getBoundingClientRect()
		const widthRatio = rect.width / 160
		const heightRatio = rect.height / 90
		console.log(evt)
		return {
			x: ((evt.clientX || evt.touches[0].clientX) - rect.left) / widthRatio,
			y: ((evt.clientY || evt.touches[0].clientY) - rect.top) / heightRatio
		}
	}
	
	function getHold(pos) {
		for (let i = 0; i < holds.length; i++) {
			const h = holds[i]
			
			if ((Math.pow((pos.x - h.x), 2) + Math.pow((pos.y - h.y), 2)) < Math.pow(3, 2)) {
				return h
			}
		}
	}
	
	function createHold(pos) {
		holds.push({
			x: pos.x,
			y: pos.y,
		})
		
		requestAnimationFrame(() => draw())
		updateInput()
	}
	
	function deleteHold(h) {
		holds.splice(holds.indexOf(h), 1)
		requestAnimationFrame(() => draw())
		updateInput()
	}
	
	
	const holdHaloPath = new Path2D()
	holdHaloPath.arc(0,0,3,0, 2 * Math.PI)	
	
	const holdPath = new Path2D()
	holdPath.arc(0, 0, 1, 0, 2 * Math.PI)
	
	function draw() {
		ctx.save()
		ctx.clearRect(0,0, canvas.width, canvas.height)
		ctx.fillStyle = 'rgba(0,0,0,0.4)'
		ctx.fillRect(0, 0, canvas.width, canvas.height)
		
		ctx.save()
		ctx.globalCompositeOperation = 'destination-out'
		
		ctx.fillStyle = "red"
		holds.forEach(h => {
			ctx.save()
			ctx.translate(h.x, h.y)
			ctx.fill(holdHaloPath)
			ctx.restore()
		})
		ctx.restore()
		ctx.strokeStyle = 'red'
		holds.forEach(h => {
			ctx.save()
			ctx.translate(h.x, h.y)
			ctx.stroke(holdPath)
			ctx.restore()
		})
		ctx.restore()
	}
	
	function updateInput() {
		holdsInput.value = JSON.stringify(holds)
	}
	
	draw()
}

initCanvas()

