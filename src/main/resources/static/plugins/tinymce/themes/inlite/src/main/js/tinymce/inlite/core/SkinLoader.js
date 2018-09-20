/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/SkinLoader', [
	'global!tinymce.EditorManager',
	'global!tinymce.DOM'
], function (EditorManager, DOM) {
	var fireSkinLoaded = function (editor, callback) {
		var done = function () {
			editor.fire('SkinLoaded');
			callback();
		};

		if (editor.initialized) {
			done();
		} else {
			editor.on('init', done);
		}
	};

	var load = function (editor, skin, callback) {
		var baseUrl = EditorManager.baseURL;
		var skinUrl = baseUrl + '/skins/' + skin;

		var done = function () {
			fireSkinLoaded(editor, callback);
		};

		DOM.styleSheetLoader.load(skinUrl + '/skin.min.css', done);
		editor.contentCSS.push(skinUrl + '/content.inline.min.css');
	};

	return {
		load: load
	};
});


