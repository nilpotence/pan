class DeleteDialog {
	constructor() {
		this.dialog = document.querySelector('#confirmDeleteDialog')
		this.boulderNameAnchors = this.dialog.querySelectorAll('.boulder-name')
		this.deleteForm = this.dialog.querySelector("#deleteForm")
		this.boulderId = null
		this.boulderName = null
		
		this.cancelAction = this.dialog.querySelector("#cancelDeleteAction")	
		this.confirmAction = this.dialog.querySelector("#confirmDeleteAction")
		
		this.cancelAction.addEventListener('click', () => this.cancel())
		this.confirmAction.addEventListener('click', () => this.confirm())
	}
	
	open (elem) {
		this.boulderId = elem.dataset.boulderId
		this.boulderName = elem.dataset.boulderName
		this.boulderNameAnchors.forEach(b => b.innerHTML = this.boulderName)

		this.dialog.open = true
	}
	
	cancel () {
		this.boulderId = null
		this.boulderName = null
		this.boulderNameAnchors.forEach(b => b.innerHTML = "")
		
		this.dialog.open = false
	}
	
	confirm () {
		if (!this.dialog.open) return
		
		this.deleteForm.setAttribute('action', `/boulders/${this.boulderId}`)
		this.deleteForm.submit()	
	}
}

const deleteDialog = new DeleteDialog()

document.querySelectorAll('.delete-boulder-action').forEach(elem => {
	elem.addEventListener('click', () => deleteDialog.open(elem))
})