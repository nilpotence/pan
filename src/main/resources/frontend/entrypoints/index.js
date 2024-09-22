//import * as Turbo from "@hotwired/turbo"
import '@picocss/pico'

import "./index.css"

import ready from "../components/ready.js"

ready(() => {
	const userDialog = document.querySelector("#userDialog")
	
	if (!userDialog) return 

	const userDialogOpenBtn = document.querySelector("#userDialogOpenBtn")
	const userDialogBackBtn = userDialog.querySelector("#userDialogBackBtn")
	const logoutBtn = userDialog.querySelector("#logoutBtn")
	const logoutForm = userDialog.querySelector("#logoutForm")
	
	logoutBtn.addEventListener('click', evt => logoutForm.submit())
	
	userDialogOpenBtn.addEventListener('click', evt => userDialog.open = true)
	userDialogBackBtn.addEventListener('click', evt => userDialog.open = false)
		
})