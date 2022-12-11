//import * as Turbo from "@hotwired/turbo"
import '@picocss/pico'

import "./index.css"

window.logout = () => {
	const form = document.querySelector("#logoutForm")
	if (form) form.submit()
}