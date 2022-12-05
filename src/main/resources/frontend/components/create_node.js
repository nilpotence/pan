export default function createNode(htmlString) {
	const template = document.createElement('template')
	htmlString = htmlString.trim()
	template.innerHTML = htmlString
	return template.content.firstChild
}

export function createSVGNode(svgString) {
	const template = document.createElementNS('http://www.w3.org/2000/svg', 'g')
	template.innerHTML = svgString
	return template.firstChild
}