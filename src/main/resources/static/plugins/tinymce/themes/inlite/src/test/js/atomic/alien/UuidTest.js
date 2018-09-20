/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

test('atomic/alien/UuidTest', [
	'tinymce/inlite/alien/Uuid'
], function (Uuid) {
	var testUuid = function () {
		assert.eq(Uuid.uuid('mce').indexOf('mce'), 0);
		assert.eq(Uuid.uuid('mce') !== Uuid.uuid('mce'), true);
	};

	testUuid();
});
