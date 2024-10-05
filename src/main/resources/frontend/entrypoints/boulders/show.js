import ready from '../../components/ready'
import logError from '../../components/send_client_error'

ready(() => {
	class DeleteDialog {
		constructor() {
			this.dialog = document.querySelector('#confirmDeleteDialog')
			
			if (!this.dialog) return 
			
			dialogPolyfill.registerDialog(this.dialog)
			
			this.deleteForm = this.dialog.querySelector("#deleteForm")
			
			this.cancelAction = this.dialog.querySelector("#cancelDeleteAction")	
			this.confirmAction = this.dialog.querySelector("#confirmDeleteAction")
			
			this.cancelAction.addEventListener('click', () => this.cancel())
			this.confirmAction.addEventListener('click', () => this.confirm())
		}
		
		open () {
			this.dialog.show()
		}
		
		cancel () {
			this.dialog.close()
		}
		
		confirm () {
			if (!this.dialog.open) return
			
			this.deleteForm.submit()	
		}
	}

	class TickDialog {
		constructor() {
			this.dialog = document.querySelector("#tickDialog")
			
			if (!this.dialog) return

			dialogPolyfill.registerDialog(this.dialog)
		
			this.tickForm = this.dialog.querySelector("form#tick")

			this.cancelAction = this.dialog.querySelector("#cancelTickAction")	
			this.confirmAction = this.dialog.querySelector("#confirmTickAction")
		
			this.cancelAction.addEventListener('click', () => this.cancel())
			this.confirmAction.addEventListener('click', () => this.confirm())
		}
	
		open () {
			this.dialog.show()
		}
	
		cancel () {
			this.dialog.close()
		}
	
		confirm () {
			if (!this.dialog.open) return
			
			this.tickForm.submit()	
		}
	}

	class ObsoleteDialog {
		constructor() {
			this.dialog = document.querySelector("#obsoleteDialog")
		 
			if (!this.dialog) return;

			dialogPolyfill.registerDialog(this.dialog)
		
			this.obsoleteForm = this.dialog.querySelector("form#obsoleteForm")
			this.cancelAction = this.dialog.querySelector("#cancelObsoleteAction")
			this.confirmAction = this.dialog.querySelector("#confirmObsoleteAction")

			this.unobsoleteAction = this.dialog.querySelector("#unobsoleteAction")
			this.unobsoleteForm = this.dialog.querySelector("form#unobsoleteForm")

			this.cancelAction.addEventListener('click', () => this.cancel())
			this.confirmAction.addEventListener('click', () => this.confirm())
			if (this.unobsoleteAction !== null) {
				this.unobsoleteAction.addEventListener('click', () => this.unobsolete())
			}
		}
	
		open () {
			this.dialog.show()
		}
	
		cancel () {
			this.dialog.close()
		}

		unobsolete () {
			if (!this.dialog.open) return
		
			if (this.unobsoleteForm !== null) {
				this.unobsoleteForm.submit()
			}	
		}
	
		confirm () {
			if (!this.dialog.open) return
			
			this.obsoleteForm.submit()	
		}
	}

	const obsoleteDialog = new ObsoleteDialog()

	document.querySelectorAll(".obsolete-boulder-action").forEach(elem => {
		logError("found one obsolete boulder action")
		elem.addEventListener('click', () => {
			logError("Clicked obsolete boulder action")
			obsoleteDialog.open(elem)
		})
	})


	const deleteDialog = new DeleteDialog()

	document.querySelectorAll('.delete-boulder-action').forEach(elem => {
		logError("found one delete boulder action")
		elem.addEventListener('click', () => {
			logError("clicked delete action")
			deleteDialog.open(elem)
		})
	})

	const tickDialog = new TickDialog()

	document.querySelectorAll('.edit-tick-action').forEach(elem => {
		logError("found one edit tick action")
		elem.addEventListener('click', evt => {
			logError("clicked tick action")
			tickDialog.open()
		})
	})
	
	logError("dialog init complete !")
})

