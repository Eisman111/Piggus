/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

configure({
  configs: [
    './prod.js'
  ],
  sources: [
    source('amd', 'tinymce/inlite/Demo', '../../src/demo/js', mapper.hierarchical)
  ]
});