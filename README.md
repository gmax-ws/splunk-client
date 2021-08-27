[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
# Splunk Java Client
## Copyright (C) 2021 Scalable Solutions, all rights reserved.

version: 1.0.0  
date: Wednesday, June 2, 2021  
author: Marius Gligor    
contact: <marius.gligor@gmail.com>

A Java Splunk client. How to start in Ubuntu Linux

- start Splunk in a Docker container:
```sh
$ cd splunk
$ /deploy.sh
```
- open [Splunk indexer web interface](https://127.0.0.1:8088) in a web browser and export SSL certificate in a file.
- import exported certificate into `CA Certificates KeyStore` using [KeyStore Explorer](https://keystore-explorer.org/)
- use `-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts` argument when start the application.