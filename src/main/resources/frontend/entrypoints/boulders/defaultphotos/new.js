import ready from '../../../components/ready.js'
import initTakePhoto from '../../../components/take_photo.js'

ready(() => {
	const imageFileInput = document.querySelector("input#imageFileInput")
	const imageData = document.querySelector("input#imageData")
	const canvas = document.querySelector("canvas")
	
	initTakePhoto(1600, imageFileInput, canvas, data => {
		imageData.value = data
	})
})