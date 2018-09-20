/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/SelectionMatcher', [
	'tinymce/inlite/core/Matcher',
	'tinymce/inlite/core/Measure'
], function (Matcher, Measure) {
	// textSelection :: String -> (Editor -> Matcher.result | Null)
	var textSelection = function (id) {
		return function (editor) {
			if (!editor.selection.isCollapsed()) {
				return Matcher.result(id, Measure.getSelectionRect(editor));
			}

			return null;
		};
	};

	// emptyTextBlock :: [Elements], String -> (Editor -> Matcher.result | Null)
	var emptyTextBlock = function (elements, id) {
		return function (editor) {
			var i, textBlockElementsMap = editor.schema.getTextBlockElements();

			for (i = 0; i < elements.length; i++) {
				if (elements[i].nodeName === 'TABLE') {
					return null;
				}
			}

			for (i = 0; i < elements.length; i++) {
				if (elements[i].nodeName in textBlockElementsMap) {
					if (editor.dom.isEmpty(elements[i])) {
						return Matcher.result(id, Measure.getSelectionRect(editor));
					}

					return null;
				}
			}

			return null;
		};
	};

	return {
		textSelection: textSelection,
		emptyTextBlock: emptyTextBlock
	};
});
