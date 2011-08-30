From: Andrey Somov <py4fun@gmail.com>
To: yaml-core@lists.sourceforge.net
Subject: [ANN] SnakeYAML-1.9 final is available

==========================
 Announcing SnakeYAML-1.9
==========================

A new release of SnakeYAML is now available:

    http://www.snakeyaml.org

This release delivers a public API changes as well as other changes and bug fixes.

Changes
========
 * Update public API: Introduce Yaml.loadAs(), Yaml.dumpAs() and Yaml.dumpAsMap() methods. JavaBeanLoader and JavaBeanDumper are deprecated
 
 * apply regular expression for !!float from YAML 1.2 specification
 * Provide low level methods from objects to Nodes and Events (represent(Object), serialize(Node))
 * Improved support for empty JavaBeans
 * Do not remove root tags of JavaBeans when it is not explicitly requested 
 * Generate OSGi bundle

The complete list of changes is here: http://code.google.com/p/snakeyaml/wiki/changes 

Resources
==========

SnakeYAML homepage: http://www.snakeyaml.org
SnakeYAML documentation: http://code.google.com/p/snakeyaml/wiki/Documentation

JAR package: http://repo2.maven.org/maven2/org/yaml/snakeyaml/1.9/snakeyaml-1.9.jar
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
