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

const deleteDialog = new DeleteDialog()

document.querySelectorAll('.delete-boulder-action').forEach(elem => {
	elem.addEventListener('click', () => deleteDialog.open(elem))
})