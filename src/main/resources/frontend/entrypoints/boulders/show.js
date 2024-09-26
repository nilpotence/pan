class DeleteDialog {
	constructor() {
		this.dialog = document.querySelector('#confirmDeleteDialog')
		
		if (!this.dialog) return 
		
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

class ObsoleteDialog {
	constructor() {
		this.dialog = document.querySelector("#obsoleteDialog")
		 
		if (!this.dialog) return;
		
		this.obsoleteForm = this.dialog.querySelector("form#obsoleteForm")
		this.cancelAction = this.dialog.querySelector("#cancelObsoleteAction")
		this.confirmAction = this.dialog.querySelector("#confirmObsoleteAction")

		this.unobsoleteAction = this.dialog.querySelector("#unobsoleteAction")
		this.unobsoleteForm = this.dialog.querySelector("form#unobsoleteForm")

		this.cancelAction.addEventListener('click', () => this.cancel())
		this.confirmAction.addEventListener('click', () => this.confirm())
		this.unobsoleteAction.addEventListener('click', () => this.unobsolete())
	}
	
	open () {
		this.dialog.open = true
	}
	
	cancel () {
		this.dialog.open = false
	}

	unobsolete () {
		if (!this.dialog.open) return
		
		this.unobsoleteForm.submit()	
	}
	
	confirm () {
		if (!this.dialog.open) return
		
		this.obsoleteForm.submit()	
	}
}

const obsoleteDialog = new ObsoleteDialog()

document.querySelectorAll(".obsolete-boulder-action").forEach(elem => {
	elem.addEventListener('click', () => obsoleteDialog.open(elem))
})


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

