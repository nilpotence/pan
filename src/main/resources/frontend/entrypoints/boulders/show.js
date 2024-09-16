class DeleteDialog {
	constructor() {
		this.dialog = document.querySelector('#confirmDeleteDialog')
		this.deleteForm = this.dialog.querySelector("#deleteForm")
		
		this.cancelAction = this.dialog.querySelector("#cancelDeleteAction")	
		this.confirmAction = this.dialog.querySelector("#confirmDeleteAction")
		
		this.cancelAction.addEventListener('click', () => this.cancel())
		this.confirmAction.addEventListener('click', () => this.confirm())
	}
	
	open () {
		this.dialog.open = true
	}
	
	cancel () {
		this.dialog.open = false
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
		
		this.tickForm = this.dialog.querySelector("form#tick")

		this.cancelAction = this.dialog.querySelector("#cancelTickAction")	
		this.confirmAction = this.dialog.querySelector("#confirmTickAction")
		
		this.cancelAction.addEventListener('click', () => this.cancel())
		this.confirmAction.addEventListener('click', () => this.confirm())
	}
	
	open () {
		this.dialog.open = true
	}
	
	cancel () {
		this.dialog.open = false
	}
	
	confirm () {
		if (!this.dialog.open) return
		
		this.tickForm.submit()	
	}
}

const deleteDialog = new DeleteDialog()

document.querySelectorAll('.delete-boulder-action').forEach(elem => {
	elem.addEventListener('click', () => deleteDialog.open(elem))
})

const tickDialog = new TickDialog()

document.querySelectorAll('.edit-tick-action').forEach(elem => {
	elem.addEventListener('click', evt => {
		evt.preventDefault()
		tickDialog.open()
	})
})

