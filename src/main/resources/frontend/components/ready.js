export default function ready(callback) {
	if (document.readyState !== 'loading') {
		callback()
		return
	}
	
	document.addEventListener('DOMContentLoaded', callback)
}