# Selenium Test Example For PrestaShop
Test scenarios:
1. Login to sytem, valid,invalid

## Run ##
**start app:** docker compose up
Run tests headless: mvn clean test
Rud tests local: mvn clean test -Plocal

logs run to ./log


Username (uživatel): demo@prestashop.com

Password (heslo): prestashop_demo

http://localhost:8089/admin148djndmy7tdmjfjn06/index.php?controller=AdminLogin&token=0f263da2d4d85bd327906680b2b1bb02&redirect=AdminOrders