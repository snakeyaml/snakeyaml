***The art of simplicity is a puzzle of complexity.***

## CVE ##

When you are here because of some vulnerability report please be 
aware that it is most probably a **false positive**.

When you use SnakeYAML 
[to configure your application](https://bitbucket.org/snakeyaml/snakeyaml/wiki/CVE-2022-1471) 
you are totally safe.

### When a low quality tooling is complaining ###

1. Go to the issue tracker of your low quality tooling and file a bug report about a false positive. You will be impressed with the amount of bugs already created and ignored (this makes the tool low quality - the bugs are created but ignored). The big amount of already reported issues should not stop you - they must be aware of the stream of false positives they produce.
2. Go to your manager or security specialist and present this information. If you pay for the low quality tooling they cannot leave it unattended.
3. Develop further and be happy !

## Overview ##
[YAML](http://yaml.org) is a data serialization format designed for human readability and interaction with scripting languages.

SnakeYAML is a YAML 1.1 processor for the Java Virtual Machine version 8+.
For YAML 1.2 (which is a superset of JSON) you may have a look at [SnakeYAML Engine](https://bitbucket.org/snakeyaml/snakeyaml-engine)

## SnakeYAML features ##

* a **complete** [YAML 1.1 processor](http://yaml.org/spec/1.1/current.html). (If you need YAML **1.2** support have a look [here](https://bitbucket.org/snakeyaml/snakeyaml-engine)). In particular, SnakeYAML can parse all examples from the specification.
* Unicode support including UTF-8/UTF-16 input/output.
* high-level API for serializing and deserializing native Java objects.
* support for all types from the [YAML types repository](http://yaml.org/type/index.html).
* relatively sensible error messages.
* when you plan to feed the parser with untrusted data please study the settings which allow to restrict incoming data.


## Info ##
 * [Changes](https://bitbucket.org/snakeyaml/snakeyaml/wiki/Changes)
 * [Documentation](https://bitbucket.org/snakeyaml/snakeyaml/wiki/Documentation)
 * [CVE and untrusted data](https://bitbucket.org/snakeyaml/snakeyaml/wiki/CVE%20&%20NIST.md)

## Contribute ##
* GIT [is now used](https://bitbucket.org/snakeyaml/snakeyaml/wiki/Migration%20to%20Git) to dance with the [source code](https://bitbucket.org/snakeyaml/snakeyaml/src).
* If you find a bug in SnakeYAML, please [file a bug report](https://bitbucket.org/snakeyaml/snakeyaml/issues?status=new&status=open&is_spam=!spam).
* You may discuss SnakeYAML at
[the mailing list](http://groups.google.com/group/snakeyaml-core).
* [Slack workspace](https://app.slack.com/client/T26CKL7FU/D02URJSL2KS)
* Telegram group is removed because of the spam
* [YAML community](https://matrix.to/#/%23chat:yaml.io)
