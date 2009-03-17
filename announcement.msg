From: Andrey Somov <py4fun@gmail.com>
To: yaml-core@lists.sourceforge.net
Subject: [ANN] SnakeYAML-1.1: improve performance and test coverage

=============================
 Announcing SnakeYAML-1.1
=============================

A new release of SnakeYAML is now available:

    http://trac-hg.assembla.com/snakeyaml/

This release delivers a significant performance improvements as well as minor changes
and bug fixes.

Changes
=======
 * Test coverage reached 98%
 * Better Spring support: the root JavaBean class can be specified as a String
 * Performance: fix a bug with expanding Regular Expressions
 * Fix ticket #4: java.sql.Date was not handled properly
 * Introduce Enums in DumperOptions
 * Minor bug fixes 

Resources
=========

SnakeYAML homepage: http://trac-hg.assembla.com/snakeyaml/
SnakeYAML documentation: http://trac-hg.assembla.com/snakeyaml/wiki/Documentation

JAR package: http://snakeyamlrepo.appspot.com/repository/SnakeYAML/SnakeYAML/1.1/SnakeYAML-1.1.jar
Reports: http://snakeyamlrepo.appspot.com/

SnakeYAML Mercurial repository: http://hg.assembla.com/snakeyaml
Submit a bug report: http://trac-hg.assembla.com/snakeyaml/newticket

YAML homepage: http://yaml.org/
YAML-core mailing list: http://lists.sourceforge.net/lists/listinfo/yaml-core


About SnakeYAML
============

YAML is a data serialization format designed for human readability and
interaction with scripting languages.  SnakeYAML is a YAML parser and
emitter for Java.

SnakeYAML features a complete YAML 1.1 parser.
SnakeYAML is applicable for a broad range of tasks from complex
configuration files to object serialization and persistence.


Copyright
=========

SnakeYAML is developed by Andrey Somov <py4fun@gmail.com>.

SnakeYAML is released under the MIT license.
