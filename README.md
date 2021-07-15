# Hoster

A simple desktop application for configuring domains, virtual hosts and PHP settings, also manage 
some Apache Server basics statistics.

![](https://i.ibb.co/NtmmLgg/hoster.png)

## Features

- Multiplatform: you can use it on Windows, Linux and (probably) MAC systems.
- No installation necessary: just use the executable `.exe` for Windows and `.jar` for Linux and MAC.
- Stupidly [easy to use](https://github.com/dprietob/hoster#usage)
- Supports Apache servers and PHP as stack (for now).

## Installation

For Windows, Hoster uses `httpd -k restart` to restart Apache Server. 
If this command fails, Hoster runs `httpd -k install` which register Apache
as a Service in the system. This can be avoided if `httpd.exe` is added to
`PATH` being accessible through console globally.

## Usage

A GIF is worth a thousand words:

![](https://i.ibb.co/dcKHSFz/usage.gif)

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

## License

This project is licensed under the terms of the MIT license.
