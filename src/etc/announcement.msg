From: Andrey Somov <py4fun@gmail.com>
To: yaml-core@lists.sourceforge.net
Subject: [ANN] SnakeYAML-1.7 final is available

==========================
 Announcing SnakeYAML-1.7
==========================

A new release of SnakeYAML is now available:

    http://code.google.com/p/snakeyaml/

This release delivers simplified public API (Loader and Dumper are deprecated, issue 77),
as well as other minor changes and bug fixes.

Changes
========
 * fix for issue 74: Do not use redundant tags for arrays which are JavaBean properties,
 * fix for issue 73: Provide support for loading java.util.Set as a sequence,
 * fix for issue 55: Allow direct field access bypassing setters and getters,
 * fix for issue 53: Enhancement for a pretty format that combines BLOCK and FLOW

The complete list of changes is here: http://snakeyamlrepo.appspot.com/releases/1.7/site/changes-report.html 

Resources
==========

SnakeYAML homepage: http://code.google.com/p/snakeyaml/
SnakeYAML documentation: http://code.google.com/p/snakeyaml/wiki/Documentation

JAR package: http://snakeyamlrepo.appspot.com/repository/org/yaml/snakeyaml/1.7/snakeyaml-1.7.jar
Reports: http://snakeyamlrepo.appspot.com/

SnakeYAML Mercurial repository: http://snakeyaml.googlecode.com/hg/
Submit a bug report: http://code.google.com/p/snakeyaml/issues/entry

YAML homepage: http://yaml.org/
YAML-core mailing list: http://lists.sourceforge.net/lists/listinfo/yaml-core

About SnakeYAML
================

SnakeYAML is a YAML parser and emitter for Java 5.

SnakeYAML features a complete YAML 1.1 parser.
SnakeYAML is applicable for a broad range of tasks from complex
configuration files to object serialization and persistence.

Copyright
==========
SnakeYAML is released under the Apache License Version 2.0
