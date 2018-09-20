/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/Measure', [
	'global!tinymce.DOM',
	'global!tinymce.geom.Rect',
	'tinymce/inlite/core/Convert'
], function (DOM, Rect, Convert) {
	var toAbsolute = function (rect) {
		var vp = DOM.getViewPort();

		return {
			x: rect.x + vp.x,
			y: rect.y + vp.y,
			w: rect.w,
			h: rect.h
		};
	};

	var measureElement = function (elm) {
		var clientRect = elm.getBoundingClientRect();

		return toAbsolute({
			x: clientRect.left,
			y: clientRect.top,
			w: Math.max(elm.clientWidth, elm.offsetWidth),
			h: Math.max(elm.clientHeight, elm.offsetHeight)
		});
	};

	var getElementRect = function (editor, elm) {
		return measureElement(elm);
	};

	var getPageAreaRect = function (editor) {
		return measureElement(editor.getElement().ownerDocument.body);
	};

	var getContentAreaRect = function (editor) {
		return measureElement(editor.getContentAreaContainer() || editor.getBody());
	};

	var getSelectionRect = function (editor) {
		var clientRect = editor.selection.getBoundingClientRect();
		return clientRect ? toAbsolute(Convert.fromClientRect(clientRect)) : null;
	};

	return {
		getElementRect: getElementRect,
		getPageAreaRect: getPageAreaRect,
		getContentAreaRect: getContentAreaRect,
		getSelectionRect: getSelectionRect
	};
});
