/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/file/Picker', [
	'global!tinymce.util.Promise'
], function (Promise) {
	var pickFile = function () {
		return new Promise(function (resolve) {
			var fileInput;

			fileInput = document.createElement("input");
			fileInput.type = "file";
			fileInput.style.position = 'fixed';
			fileInput.style.left = 0;
			fileInput.style.top = 0;
			fileInput.style.opacity = 0.001;
			document.body.appendChild(fileInput);

			fileInput.onchange = function(e) {
				resolve(Array.prototype.slice.call(e.target.files));
			};

			fileInput.click();
			fileInput.parentNode.removeChild(fileInput);
		});
	};

	return {
		pickFile: pickFile
	};
});


