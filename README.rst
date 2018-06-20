REST Emailer Plug-in
====================

.. image:: https://travis-ci.org/curityio/rest-mailer.svg?branch=dev
     :target: https://travis-ci.org/curityio/rest-mailer

This project provides an open source email plugin for the Curity Identity Server. It is provided purely as an example without support to show how you can create a tailor made plugin that handles email delivery by calling a REST API.

System Requirements
~~~~~~~~~~~~~~~~~~~
* Curity Identity Server 3.1.0 and `its system requirements <https://developer.curity.io/docs/latest/system-admin-guide/system-requirements.html>`_

Requirements for Building from Source
"""""""""""""""""""""""""""""""""""""
* Maven 3
* Java JDK v. 8

Compiling the Plug-in from Source
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
The project can be built using Maven. Use the command ``mvn package`` to find the resulting JARs in the ``target/libs`` directory.

Installation
~~~~~~~~~~~~
To install the plugin, you need the binary files. You can build these yourself, as described in the previous section, or
you can download it from the `releases section of this project's GitHub repository <https://github.com/curityio/rest-mailer/releases>`_.

Take the resulting JAR-files and copy them into the directory ``${IDSVR_HOME}/usr/share/plugins/restmailer``.

Note that the ``/restmailer`` directory can be named freely; using (a reference to) the name of the plugin just makes it
easier to find the files later on.

The plugin will become available after you (re)start the Curity Identity Server.

.. note::

    The JAR file needs to be deployed to each run-time node and the admin node. For simple test deployments where the admin node is a run-time node, the JAR file only needs to be copied to one location.

For a more detailed explanation of installing plug-ins, refer to the `Curity developer guide <https://developer.curity.io/docs/latest/developer-guide/plugins/index.html#plugin-installation>`_.

Configuring the REST Mailer in Curity
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Configuration using the Admin UI
""""""""""""""""""""""""""""""""
Once installed, the REST mailer becomes available as email provider. An email provider is configured as a facility. To configure an email provider, open the facilities through
the ``Facilities``-button on the top right and look for the ``Email`` heading. When editing an Email provider, the type ``rest-mailer`` will configure the REST Emailer plugin.


License
~~~~~~~

This plugin and its associated documentation is listed under the `Apache 2 license <LICENSE>`_.

More Information
~~~~~~~~~~~~~~~~

Please visit `curity.io <https://curity.io/>`_ for more information about the Curity Identity Server.

Copyright (C) 2018 Curity AB.
