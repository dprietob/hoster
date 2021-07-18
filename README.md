# Hoster

A simple desktop application for configuring domains, virtual hosts and PHP settings, also manage 
some Apache Server basics statistics.

![](https://i.ibb.co/y4Z6krF/hoster.png)

## Features

- Multiplatform: you can use it on Windows, Linux and (probably) MAC systems.
- No installation necessary: just use the executable `.exe` for Windows and `.jar` for Linux and MAC.
- Stupidly [easy to use](https://github.com/dprietob/hoster#usage)
- Supports Apache servers and PHP as stack (for now).

## Installation

Obviously, in order to use Hoster, it's necessary have installed Apache Server 
and PHP in your computer. 

In Windows systems, Hoster uses `httpd -k restart` to restart Apache Server. 
If this command fails, Hoster runs `httpd -k install` which register Apache
as a Service in the system. This can be avoided if `httpd.exe` is added to
`PATH` being accessible through console globally.

For Linux systems, Hoster uses `service apache2 status` and `service apache2 restart`
commands to manage Apache Server, so they need to be accessible through terminal.

To compile, it's necessary add `lib/flatlaf-1.3.jar` and `lib/ini4j-0.5.5.jar` as 
library in your IDE or code editor and configure `Java 1.8` as minimum JDK.

## Caution

Hoster will overwrite hosts, virtual-host and php.ini files, so it's recommended 
to make backups before use it. As an improvement, a backup feature will be added
in the future.

## Contributing

#### Bug Reports & Feature Requests

Please use the [issue tracker](https://github.com/dprietob/hoster/issues) to report 
any bugs or file feature requests.

#### Developing

PRs are welcome. To begin developing, do this:

```bash
$ git clone git@github.com:dprietob/hoster.git
$ cd hoster
```

## Dependencies

Hoster uses [FlatLaf](https://github.com/JFormDesigner/FlatLaf) to GUI customization,
[ini4j](https://sourceforge.net/projects/ini4j/) to .ini file manipulation
and also some [FarmFresh icons](https://www.fatcow.com/free-icons). 

## License

This project is licensed under the terms of the MIT license.
