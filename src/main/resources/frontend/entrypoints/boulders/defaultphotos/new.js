import ready from '../../../components/ready.js'

ready(() => {
	const imageFileInput = document.querySelector("input#imageFileInput")
	const imageData = document.querySelector("input#imageData")
	const canvas = document.querySelector("canvas")
	
	imageFileInput.addEventListener("change", evt => {
		const ctx = canvas.getContext("2d")
		const image = new Image()
		image.src = URL.createObjectURL(evt.target.files[0])
		
		image.onload = () => {
		
			const width = 1600
			const height = (image.height / image.width) * width
		
			canvas.width = width
			canvas.height = height
			canvas.style.width = "100%"
		
			ctx.drawImage(image, 0, 0, width, height)
			const data = canvas.toDataURL("image/jpeg", 0.6)
			imageData.value = data
		}
		
	})
})