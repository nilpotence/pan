//import * as Turbo from "@hotwired/turbo"

import '@picocss/pico'
// Only the icons the templates use, bundled instead of one <link> each.
import 'css.gg/icons/css/trash.css'
import 'css.gg/icons/css/pen.css'
import 'css.gg/icons/css/arrow-left.css'
import 'css.gg/icons/css/arrow-right.css'
import "./index.css"

import ready from "../components/ready.js"
import logError from "../components/send_client_error.js"

window.onerror = function (message, url, lineNumber) {
	logError("[url="+url+", line="+lineNumber+"] " + message)
}

// Boulder list sort selector (kept out of the template: no inline scripts under the CSP)
ready(() => {
	const sortSelect = document.querySelector("#sortSelect")
	
	if (!sortSelect) return
	
	sortSelect.addEventListener('change', evt => window.location = "?sort=" + evt.target.value)
})

ready(() => {
	const userDialog = document.querySelector("#userDialog")
	
	if (!userDialog) return 
	
	const userDialogOpenBtn = document.querySelector("#userDialogOpenBtn")
	const userDialogBackBtn = userDialog.querySelector("#userDialogBackBtn")
	const logoutBtn = userDialog.querySelector("#logoutBtn")
	const logoutForm = userDialog.querySelector("#logoutForm")
	
	logoutBtn.addEventListener('click', evt => logoutForm.submit())
	
	userDialogOpenBtn.addEventListener('click', evt => userDialog.show())
	userDialogBackBtn.addEventListener('click', evt => userDialog.close())
		
})
