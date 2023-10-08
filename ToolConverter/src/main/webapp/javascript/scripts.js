// Selectors
const dropArea = document.querySelector(".drag-area");
const dragText = document.querySelector(".header");
const input = document.querySelector("input[type='file']");
const button = dropArea.querySelector(".button");
const progress = document.querySelector(".progress");
const progressPercent = document.querySelector(".progress-percent");
const resetButton = document.getElementById("resetButton");
const exampleButton = document.getElementById("exampleButton");
const activityItems = document.getElementById("activityItems");
const clearActivityButton = document.getElementById("clearActivityButton");
const sortSelect = document.getElementById("sort-select");
const sortDropdown = document.querySelector(".sort-dropdown");
const deleteAllButton = document.getElementById("deleteAllButton");

let files = [];

// Load saved activities from localStorage
loadSavedActivities();

//Functions

function addFileToList(file) {
	files.push(file);
	renderFileList();
	addToActivityList(`File uploaded: ${file.name}`);
}

function renderFileList() {
  const fileList = document.getElementById("fileList");
  fileList.innerHTML = "";

  files.forEach((file) => {
    const listItem = document.createElement("li");
    listItem.textContent = `${file.name} - ${(file.size / 1024).toFixed(2)} KB`;

    const fileActionButtons = document.createElement("div");
    fileActionButtons.classList.add("file-action-buttons");

    const convertButtonXml = createConvertButton(file, "XML");
    const convertButtonJson = createConvertButton(file, "JSON");
    const convertButtonPdf = createConvertButton(file, "PDF"); // Aggiungi questa linea

    const downloadButton = createDownloadButton(file);
    const removeButton = document.createElement("button");
    removeButton.innerHTML = '<i class="fas fa-trash-alt fa-lg"></i>';
    removeButton.classList.add("file-action-button", "remove-button");
    removeButton.addEventListener("click", () => {
      files = files.filter((f) => f !== file);
      renderFileList();
    });

    // Aggiungi tooltip alle icone
    convertButtonXml.title = "XML";
    convertButtonJson.title = "JSON";
    convertButtonPdf.title = "PDF"; // Aggiungi questa linea
    downloadButton.title = "Excel";
    removeButton.title = "Delete";

    fileActionButtons.appendChild(convertButtonXml);
    fileActionButtons.appendChild(convertButtonJson);
    fileActionButtons.appendChild(convertButtonPdf); // Aggiungi questa linea
    fileActionButtons.appendChild(downloadButton);
    fileActionButtons.appendChild(removeButton);

    listItem.appendChild(fileActionButtons);
    fileList.appendChild(listItem);
  });
}


function sortFilesBy(property) {
	files.sort((a, b) => {
		if (property === 'name') {
			const nameA = a.name.toLowerCase();
			const nameB = b.name.toLowerCase();
			if (nameA < nameB) return -1;
			if (nameA > nameB) return 1;
			return 0;
		} else if (property === 'size') {
			return a.size - b.size;
		}
	});

	renderFileList();
}

function createConvertButton(file, type) {
  const convertButton = document.createElement("button");
  convertButton.classList.add("convert-button");
  convertButton.addEventListener("click", () => {
    if (type === "XML") {
      convertFileToXml(file);
    } else if (type === "JSON") {
      convertFileToJson(file);
    } else if (type === "PDF") { // Aggiungi questa parte
      convertFileToPdf(file);
    }
  });

  if (type === "XML") {
    convertButton.innerHTML += '<i class="fas fa-file-code fa-lg"></i>';
  } else if (type === "JSON") {
    convertButton.innerHTML += '<i class="fas fa-file-code fa-lg json-icon"></i>';
  } else if (type === "PDF") { // Aggiungi questa parte
    convertButton.innerHTML += '<i class="far fa-file-pdf fa-lg"></i>';
  }

  return convertButton;
}

function convertFileToPdf(file) {
  // Mostra il loader
  const loader = document.getElementById("loader");
  loader.style.display = "block";

  try {
    const reader = new FileReader();
    reader.onload = function () {
      const data = new Uint8Array(reader.result);

      // Usa SheetJS per leggere il file Excel
      const workbook = XLSX.read(data, { type: "array" });

      // Seleziona il primo foglio del file Excel
      const sheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[sheetName];

      // Crea una tabella HTML dal foglio Excel
      const htmlTable = XLSX.utils.sheet_to_html(worksheet);

      // Creazione di una pagina HTML completa con la tabella
      const htmlContent = `
        <html>
          <head>
            <style>
              table {
                border-collapse: collapse;
                width: 100%;
              }
              th, td {
                border: 1px solid black;
                padding: 8px;
                text-align: left;
              }
            </style>
          </head>
          <body>
            ${htmlTable}
          </body>
        </html>
      `;

      // Creazione di un iframe nascosto per la stampa
      const iframe = document.createElement("iframe");
      iframe.style.display = "none";
      document.body.appendChild(iframe);

      // Caricamento del contenuto HTML nell'iframe
      const iframeDoc = iframe.contentDocument || iframe.contentWindow.document;
      iframeDoc.open();
      iframeDoc.write(htmlContent);
      iframeDoc.close();

      // Stampa la pagina HTML in un file PDF
      iframe.focus();
      iframe.contentWindow.print();

      // Rimuovi l'iframe dopo la stampa
      setTimeout(() => {
        document.body.removeChild(iframe);

        // Nasconde il loader
        loader.style.display = "none";

        // Aggiungi l'attività al registro delle attività
        addToActivityList(`File converted to PDF: ${file.name}`);
      }, 1000);
    };

    reader.readAsArrayBuffer(file);
  } catch (error) {
    alert(`Errore durante la conversione in PDF: ${error.message}`);
    // Nasconde il loader in caso di errore
    loader.style.display = "none";
  }
}


function convertFileToXml(file) {

	let formData = new FormData();
	formData.append("file", file);

	// Mostra il loader
	const loader = document.getElementById("loader");
	loader.style.display = "block";

	fetch("uploadFile.action", {
		method: "POST",
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				throw new Error("Errore durante la conversione del file in XML.");
			}
			return response.blob();  // restituisce un oggetto Blob
		})
		.then(blob => {
			// Crea un URL per il Blob
			let fileURL = URL.createObjectURL(blob);

			// Scarica il file XML
			let a = document.createElement("a");
			a.href = fileURL;
			a.download = "output.xml";
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);

			// Nasconde il loader
			loader.style.display = "none";
		})
		.catch(error => {
			alert(error.message);
			// Nasconde il loader in caso di errore
			loader.style.display = "none";

		});

	addToActivityList(`File converted to XML: ${file.name}`);
}

function convertFileToJson(file) {
    // Mostra il loader
    const loader = document.getElementById("loader");
    loader.style.display = "block";

    const reader = new FileReader();

    reader.onload = (event) => {
        const data = event.target.result;
        const workbook = XLSX.read(data, { type: "binary" });

        // Supponendo che tu voglia convertire il primo foglio Excel in JSON
        const firstSheetName = workbook.SheetNames[0];
        const jsonData = XLSX.utils.sheet_to_json(workbook.Sheets[firstSheetName]);

        // Ora hai il JSON risultante dal file Excel
        console.log(jsonData);

        // Chiama la funzione downloadJsonFile per scaricare il JSON
        downloadJsonFile(jsonData, "output.json");

        // Nascondi il loader dopo aver completato la conversione
        loader.style.display = "none";
    };

    reader.readAsBinaryString(file);
}


function downloadJsonFile(jsonData, fileName) {
    // Mostra il loader
    const loader = document.getElementById("loader");
    loader.style.display = "block";

    // Crea un oggetto Blob dal JSON
    const jsonBlob = new Blob([JSON.stringify(jsonData, null, 2)], { type: "application/json" });

    // Crea un URL per il Blob
    const jsonBlobURL = URL.createObjectURL(jsonBlob);

    // Crea un elemento "a" per il download
    const a = document.createElement("a");
    a.href = jsonBlobURL;
    a.download = fileName || "download.json";

    // Simula il clic sull'elemento "a" per avviare il download
    a.click();

    // Rilascia l'URL del Blob quando hai finito
    URL.revokeObjectURL(jsonBlobURL);

    // Nascondi il loader dopo aver completato il download
    loader.style.display = "none";
}


function createDownloadButton(file) {
    const downloadButton = document.createElement("button");
    downloadButton.innerHTML = '<i class="fas fa-file-excel fa-lg"></i>';
    downloadButton.addEventListener("click", () => {
        // Mostra il loader
        const loader = document.getElementById("loader");
        loader.style.display = "block";

        const fileURL = URL.createObjectURL(file);
        const a = document.createElement("a");
        a.href = fileURL;
        a.download = file.name;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

        addToActivityList(`File downloaded: ${file.name}`);

        // Nascondi il loader dopo aver completato il download
        loader.style.display = "none";
    });
    return downloadButton;
}


function startProgressBar() {
	let width = 0;

	const interval = setInterval(() => {
		if (width >= 100) {
			clearInterval(interval);

			setTimeout(() => {
				activateTab(document.querySelector('.tab-item[data-target="#tab2"]'));
			}, 800);
		} else {
			width++;
			progress.style.width = width + "%";
			progressPercent.textContent = width + "%";
		}
	}, 10);


}

function resetProgressBar() {
	progress.style.width = "0%";
	progressPercent.textContent = "0%";
}

function showProgressBar() {
	const progressContainer = document.querySelector(".progress-container");
	progressContainer.style.display = "flex";
}

function resetTool() {
	// Reset file
	file = null;
	// Reset input
	input.value = null;
	// Reset progress bar
	resetProgressBar();
	document.querySelector(".progress-container").style.display = "none";
	// Reset drop area
	dropArea.classList.remove("active");
	dragText.textContent = "Drag & Drop";
	// Remove all files from the file list
	const fileList = document.getElementById("fileList");
	while (fileList.firstChild) {
		fileList.firstChild.remove();
	}
	// Reset files array
	files = [];

	const container = document.querySelector(".container");
	container.style.backgroundColor = ""; // Imposta il colore predefinito vuoto per ripristinare il valore di default
}
resetButton.addEventListener("click", resetTool);

// Funzione per attivare le schede al click
function activateTab(tab) {
	const target = tab.dataset.target;
	const tabContent = document.querySelector(target);

	// Disattiva tutte le altre schede e contenuti delle schede
	const allTabs = document.querySelectorAll('.tab-item');
	const allTabContents = document.querySelectorAll('.tab-content');

	for (let i = 0; i < allTabs.length; i++) {
		allTabs[i].classList.remove('active');
		allTabContents[i].classList.remove('active');
	}

	// Attiva la scheda corrente e il suo contenuto
	tab.classList.add('active');
	tabContent.classList.add('active');
}


// Aggiungi l'event listener per le schede
const tabs = document.querySelectorAll('.tab-item');
for (let i = 0; i < tabs.length; i++) {
	tabs[i].addEventListener('click', function() {
		activateTab(tabs[i]);
	});
}

function displayFile() {
	if (file) {
		dropArea.classList.add("active");

		let fileType = file.type;
		let validExtensions = [
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
		];

		if (!validExtensions.includes(fileType)) {
			alert("Formato file non valido. Caricare solo file Excel.");
			dropArea.classList.remove("active");
			resetProgressBar();
			return;
		}

		addFileToList(file);

		startProgressBar();
		showProgressBar();

		let fileReader = new FileReader();

		fileReader.onload = () => {
			let fileURL = fileReader.result;

			if (
				fileType === "application/xml" ||
				fileType === "text/xml"
			) {
				// Gestisce i file XML
				let parser = new DOMParser();
				let xmlDoc = parser.parseFromString(
					fileURL,
					"application/xml"
				);
				// Elabora xmlDoc secondo le necessità
				console.log(xmlDoc);
			} else if (
				fileType ===
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
			) {
				// Gestisce i file XML
				let workbook = XLSX.read(fileURL, { type: "binary" });
				// Processa i workbook secondo le necessità
				console.log(workbook);
			}
		};

		if (
			fileType ===
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
		) {
			fileReader.readAsArrayBuffer(file);
		} else {
			fileReader.readAsDataURL(file);
		}

		addToActivityList(`File displayed: ${file.name}`);

	}
}

function downloadFile() {
	if (!file) {
		document.getElementById("error-message").innerHTML =
			"Nessun file da scaricare.";
		return;
	}

	if (
		file.type !==
		"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
	) {
		let fileURL = URL.createObjectURL(file);
		let a = document.createElement("a");
		a.href = fileURL;
		a.download = file.name;
		document.body.appendChild(a);
		a.click();
		document.body.removeChild(a);

		addToActivityList(`File downloaded: ${file.name}`);

	} else {
		const errorMessage = document.getElementById("error-message");
		if (errorMessage) {
			errorMessage.remove();
		}
	}
}

function downloadExampleFile() {
	fetch('downloadExample.action')
		.then((response) => {
			if (!response.ok) {
				throw new Error('Errore durante il download del file di esempio');
			}
			return response.blob();
		})
		.then((blob) => {
			const url = URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = url;
			a.download = 'Example.xlsx';
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
		})
		.catch((error) => {
			console.error(`Si è verificato un errore durante il download del file di esempio: ${error}`);
		});
}

exampleButton.addEventListener('click', downloadExampleFile);

// Events
button.onclick = () => {
	input.click();
};

input.addEventListener("change", function() {
	file = this.files[0];
	dropArea.classList.add("active");
	displayFile();
});

dropArea.addEventListener("dragover", (event) => {
	event.preventDefault();
	dropArea.classList.add("active");
	dragText.textContent = "Release to Upload";
});

dropArea.addEventListener("dragleave", () => {
	dropArea.classList.remove("active");
	dragText.textContent = "Drag & Drop";
});

dropArea.addEventListener("drop", (event) => {
	event.preventDefault();
	file = event.dataTransfer.files[0];
	displayFile();
});


document.getElementById("uploadForm").addEventListener("submit", function(event) {
	event.preventDefault();

	if (!file) {
		alert("Nessun file da caricare.");
		return;
	}

	let formData = new FormData();
	formData.append("file", file);

	// Usa 'fetch' invece di 'XMLHttpRequest'
	fetch("uploadFile.action", {
		method: "POST",
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				throw new Error("Errore durante l'elaborazione del file.");
			}
			return response.blob();  // restituisce un oggetto Blob
		})
		.then(blob => {
			// Crea un URL per il Blob
			let fileURL = URL.createObjectURL(blob);
			// Scarica il file
			let a = document.createElement("a");
			a.href = fileURL;
			a.download = "output.xml";
			document.body.appendChild(a);
			a.click();
			document.body.removeChild(a);
			resetProgressBar();

		})
		.catch(error => {
			alert(error.message);
			resetProgressBar();
		});
});

function toggleTheme() {
	// Controlla lo stato del checkbox per il tema del container
	var containerToggle = document.getElementById("container-toggle");
	var container = document.querySelector(".container");

	if (containerToggle.checked) {
		container.classList.add("dark-mode");
	} else {
		container.classList.remove("dark-mode");
	}
}

function changeContainerColor() {
	var container = document.querySelector('.container');
	var colorInput = document.querySelector('#container-color');
	var color = colorInput.value;
	container.style.backgroundColor = color;
}

// Carica le impostazioni salvate dal Local Storage, se presenti
window.addEventListener('DOMContentLoaded', () => {
	const containerToggle = document.getElementById('container-toggle');
	const containerColor = document.getElementById('container-color');

	// Controlla se esistono impostazioni salvate
	if (localStorage.getItem('darkMode')) {
		const darkMode = localStorage.getItem('darkMode') === 'true';
		containerToggle.checked = darkMode;
		toggleTheme();
	}

	if (localStorage.getItem('containerColor')) {
		const color = localStorage.getItem('containerColor');
		containerColor.value = color;
		changeContainerColor();
	}

	// Carica le attività salvate dal Local Storage
	loadSavedActivities();
});

// Salva le impostazioni nel Local Storage al click del bottone "Save Settings"
function saveSettings() {
	const containerToggle = document.getElementById('container-toggle');
	const containerColor = document.getElementById('container-color');

	const darkMode = containerToggle.checked;
	const color = containerColor.value;

	localStorage.setItem('darkMode', darkMode);
	localStorage.setItem('containerColor', color);

	// Save activities to localStorage
	const activityRows = document.querySelectorAll("#activityItems tr");
	const savedActivities = Array.from(activityRows).map(row => row.textContent.trim());
	localStorage.setItem("activities", JSON.stringify(savedActivities));
}

function loadSavedActivities() {
	const savedActivities = JSON.parse(localStorage.getItem("activities")) || [];

	savedActivities.forEach((activity) => {
		addToActivityList(activity);
	});
}

function addToActivityList(activity) {
	const activityRow = document.createElement("tr");
	const activityData = document.createElement("td");
	activityData.textContent = activity;
	activityRow.appendChild(activityData);
	activityItems.appendChild(activityRow);
}

clearActivityButton.addEventListener("click", () => {
	if (confirm("Are you sure you want to clear the activity log?")) {
		// Clear the activity log
		activityItems.innerHTML = "";

		// Clear activities from localStorage
		localStorage.removeItem("activities");
	}
});


sortSelect.addEventListener('change', (event) => {
	const selectedProperty = event.target.value;
	sortFilesBy(selectedProperty);
});

sortSelect.addEventListener('click', () => {
	sortDropdown.classList.toggle('open');
});

document.addEventListener('click', (event) => {
	const targetElement = event.target;
	if (!sortDropdown.contains(targetElement)) {
		sortDropdown.classList.remove('open');
	}
});

deleteAllButton.addEventListener("click", () => {
	files = [];
	renderFileList();
});
