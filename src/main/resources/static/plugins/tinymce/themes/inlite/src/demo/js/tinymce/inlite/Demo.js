/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*eslint no-console:0 */

define('tinymce/inlite/Demo', [
	'tinymce/inlite/Theme',
	'global!tinymce'
], function(Theme, tinymce) {
	tinymce.init({
		selector: 'div.tinymce',
		theme: 'inlite',
		plugins: 'image table link paste contextmenu textpattern autolink',
		insert_toolbar: 'quickimage quicktable',
		selection_toolbar: 'bold italic | quicklink h2 h3 blockquote',
		inline: true,
		paste_data_images: true
	});

	return function() {};
});
