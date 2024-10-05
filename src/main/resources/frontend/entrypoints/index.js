//import * as Turbo from "@hotwired/turbo"

import 'dialog-polyfill/dist/dialog-polyfill.css'
import '@picocss/pico'
import "./index.css"

import ready from "../components/ready.js"
import logError from "../components/send_client_error.js"

import dialogPolyfill from 'dialog-polyfill'

window.dialogPolyfill = dialogPolyfill

window.onerror = function (message, url, lineNumber) {
	logError("[url="+url+", line="+lineNumber+"] " + message)
}

ready(() => {
	const userDialog = document.querySelector("#userDialog")
	
	if (!userDialog) return 
	
	dialogPolyfill.registerDialog(userDialog)

	const userDialogOpenBtn = document.querySelector("#userDialogOpenBtn")
	const userDialogBackBtn = userDialog.querySelector("#userDialogBackBtn")
	const logoutBtn = userDialog.querySelector("#logoutBtn")
	const logoutForm = userDialog.querySelector("#logoutForm")
	
	logoutBtn.addEventListener('click', evt => logoutForm.submit())
	
	userDialogOpenBtn.addEventListener('click', evt => userDialog.show())
	userDialogBackBtn.addEventListener('click', evt => userDialog.close())
		
})
