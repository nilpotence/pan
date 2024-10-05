let logCount = 0

export default async function(message) {
	logCount++
	try {
		message = "[" + logCount + "] " + message
		const response = await fetch('/api/errors?message=' + btoa(message))
	} catch (e) {
		console.error(e)
	}
}