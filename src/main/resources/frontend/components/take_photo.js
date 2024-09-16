export default function initTakePhoto(defaultWidth, fileInput, canvas, callback) {
	if (!canvas) canvas = document.createElement("canvas")
	
	fileInput.addEventListener("change", evt => {
		const ctx = canvas.getContext("2d")
		const image = new Image()
		image.src = URL.createObjectURL(evt.target.files[0])
		
		image.onload = () => {
			const width = defaultWidth
			const height = (image.height / image.width) * width
		
			canvas.width = width
			canvas.height = height
			canvas.style.width = "100%"
		
			ctx.drawImage(image, 0, 0, width, height)
			const data = canvas.toDataURL("image/jpeg", 0.6)
			
			fileInput.value = null
			
			callback(data, width, height)
		}
	})
}