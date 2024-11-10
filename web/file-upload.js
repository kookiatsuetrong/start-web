var FileUpload = { 
	borderColor: "goldenrod"
}

FileUpload.setStyle = function setStyle(style) {
	for (var k in style) {
		FileUpload[k] = style[k]
	}
	
	var dropArea = document.querySelector(FileUpload.element)
	for (var k in style) {
		dropArea.style[k] = style[k]
	}
}

FileUpload.create = function setup(element) {
	FileUpload.element = element
	var dropArea = document.querySelector(FileUpload.element)
	
	dropArea.style.height        = "3rem"
	dropArea.style.paddingBottom = "1rem"
	dropArea.style.display       = "block"
	dropArea.style.background    = "rgba(128, 128, 128, .25)"
	dropArea.style.border        = "0.15rem dashed #666"
	dropArea.style.borderRadius  = "0.35rem"
	dropArea.style.transition    = "border .1s linear"
	dropArea.style.textAlign     = "center"
	
	dropArea.style.borderColor = FileUpload.borderColor

	dropArea.ondrop      = this.onDrop
	dropArea.onclick     = this.onClick
	dropArea.ondragover  = this.onDragOver
	dropArea.ondragleave = this.onDragLeave
	dropArea.ondragend   = this.onDragEnd
	
	dropArea.querySelector("p").style.color = "#666"
}

FileUpload.onDragOver = function onDragOver(event) {
	event.preventDefault()
	var dropArea = document.querySelector(FileUpload.element)
	dropArea.style.border = ".15rem dashed #bbb"
	dropArea.style.background = "#f2fff2"
}

FileUpload.onDragLeave = function onDragLeave(event) {
	event.preventDefault()
	var dropArea = document.querySelector(FileUpload.element)
	dropArea.style.border = ".15rem dashed #666"
	dropArea.style.background = "rgba(128, 128, 128, .25)"
}

FileUpload.onDragEnd = function onDragEnd(event) {
	event.preventDefault()
	var dropArea = document.querySelector(FileUpload.element)
	dropArea.style.border = ".15rem dashed #666"
	dropArea.style.background = "rgba(128, 128, 128, .25)"
}

FileUpload.onReady = function onReady(event) {
	var count = 0
	var all = document.querySelectorAll("[type=file]")
	for (var e of all) {
		for (var f of e.files) {
			// console.log(f)
			count++
		}
	}
	var dropArea = document.querySelector(FileUpload.element)
	dropArea.style.border = ".15rem dashed #666"
	dropArea.style.background = "rgba(128, 128, 128, .25)"
	
	var message = count + " File(s)"
	dropArea.querySelector("p").innerText = message
	
}

FileUpload.onDrop = function onDrop(event) {			
	event.preventDefault()
	FileUpload.uploadFiles(event.dataTransfer.files)
}

FileUpload.onClick = function onClick() {
	var chooser = document.createElement("input")
	chooser.type = "file"
	chooser.multiple = true
	chooser.accept = "image/*"
	chooser.capture = true
	chooser.onchange = event => {
		FileUpload.uploadFiles(event.target.files)
	}
	chooser.click()
}

FileUpload.uploadFiles = function uploadFiles(files) {
	var random = document.querySelectorAll("input").length + 1
	var chooser = document.createElement("input")
	chooser.name = "photo-" + random
	chooser.type = "file"
	chooser.multiple = true
	// chooser.accept = "image/*"
	chooser.style.display = "none"
	chooser.files = files

	var form = document.querySelector("form")
	form.append(chooser)
	this.onReady(event)
}
