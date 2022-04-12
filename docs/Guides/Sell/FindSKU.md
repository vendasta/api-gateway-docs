# Find a SKU

We currently only support creating orders for products that don't include order forms. An API to list the SKUs from your store will be comming. In the mean time please use this list or reachout to support@vendasta.com if you can't find a product you are looking for.

The SKU is typically different between the demo and production environments so be sure to use the correct values.

## Your Private Products

Product SKUs start with `MP-`. Example: `MP-c4974d390a044c28aec31e421aa662b2`

Addon SKUs start with `A-`. Example: `A-GMXXNQ4ZGD`

Edition SKUs match the format `^MP-.*:EDITION-.*$`. Example `MP-c4974d390a044c28aec31e421aa662b2:EDITION-1234`

### Edition SKU (appId:editionId)
Navigate to [https://vendors.vendasta.com/products](https://vendors.vendasta.com/products) and login to view the products table.

Find the product you wish to build the purchase CTA for.

![Product List Page](./images/product-list.png)

Clicking on the product will bring you to the product details page.

From there, the (appId) is located in the url. It starts with `MP-`:

![Product SKU Url](./images/product-id-url.png)

If your product does not support editions the entire editionSKU is the `MP-` prefixed id.

If your app does use editions your editionSKU will have two parts joined by a colon:
 - `MP-` prefixed appId.
 - `EDITION-` prefixed editionId.
 
The editionId is just below the name (prefixed with `EDITION-`).

![Edition SKU](./images/edition-id.png)

### Addon SKU (addonId)
To obtain the addonId click the `Add-ons` tab and find the addon.
![Addon List](./images/addon-list.png)

Clicking on the addon will bring you to the addon details page.
The url will contain the addon SKU (addonId) prefixed with `A-`.

![Addon SKU](./images/addon-id-url.png)

## Vendasta Products

### Reputation Management

Product | Production SKU | Demo SKU
--------|----------------|---------
 Express |RM:EDITION-F7JZ5TV8 | RM:EDITION-38SMW45H
 Pro | RM | RM

The base Reputation Management product must be already active or included in the same order as any of the addons.

Addon | Production SKU | Demo SKU
--------|----------------|---------
Review Display Widget Pro | A-5QGW8G8VVG | A-PRLDSG2NCG
Premium Apartment Source | A-6XN73QJSG4 | A-RVQCK24434
Rapid Reviews | A-DVDGNKC4Q3 | A-DJNQ55XZ6T

### Social Marketing

Product | Production SKU | Demo SKU
--------|----------------|---------
 Express |SM:EDITION-FVGBNLVZ | SM:EDITION-SWVF3WH8
 Pro | SM | SM

### Listing Builder

Product | Production SKU | Demo SKU
--------|----------------|---------
 Listing Builder | MS | MS

The base Listing Builder product must be already active or included in the same order as any of the addons.

Addon | Production SKU | Demo SKU
--------|----------------|---------
Listing Distribution | A-GMXXNQ4ZGD | N/A
Listing Distribution \| Monthly | A-SX5MP2FB2L | N/A
Listing Sync Pro \| Australia Yearly | A-XQL2HMD6VV | N/A
Listing Sync Pro \| Australia Monthly | A-P3XFBZ6HCC | N/A
Listing Sync Pro \| Canada Yearly | A-2BRLL3FH4K | N/A
Listing Sync Pro \| Canada Monthly | A-C8GSD6X4D2 | N/A
Listing Sync Pro \| Canada Basic Yearly | A-TD5HVH6LHM | N/A
Listing Sync Pro \| Canada Basic Monthly | A-Z6S5D3QGHF | N/A
Listing Sync Pro \| France Yearly | A-C6CNBPXPCC | N/A
Listing Sync Pro \| France Monthly | A-VM8PJZ8PZ5 | N/A
Listing Sync Pro \| Germany Yearly | A-W2GGC7TJW3 | N/A
Listing Sync Pro \| Germany Monthly | A-JZGCTTMHLG | N/A
Listing Sync Pro \| Italy Yearly | A-KK3MNLFDP5 | N/A
Listing Sync Pro \| Italy Monthly | A-CDFBK8XW2W | N/A
Listing Sync Pro \| United States \| Yearly | A-TMPJGS28X7 | N/A
Listing Sync Pro \| United States \| Monthly | A-ZR7M2V6TCD | N/A
Listing Sync Pro \| United States \| Yext \| Yearly | A-WNW446NCNS | N/A
Listing Sync Pro \| United States \| Yext \| Monthly | A-8PHKXVRZFS | N/A
Listing Sync Pro \| United Kingdom \| Yearly | A-WCH8K4S8LS | N/A
Listing Sync Pro \| United Kingdom \| Monthly | A-FR72RDNMP6 | N/A

 ### Website

Product | Production SKU | Demo SKU
--------|----------------|---------
 Express |MP-ee4ea04e553a4b1780caf7aad7be07cd:EDITION-VFNL43ZF | MP-9cc9f21f0a234a46ad78087fc09f16bc:EDITION-RC58KN73
 Pro | MP-ee4ea04e553a4b1780caf7aad7be07cd | MP-9cc9f21f0a234a46ad78087fc09f16bc

### Customer Voice

Product | Production SKU | Demo SKU
--------|----------------|---------
 Express |MP-c4974d390a044c28aec31e421aa662b2:EDITION-TC8HJZNS | MP-fba21121b71148c9bb33e11fcd92d520:EDITION-4WWZC3RJ
 Pro | MP-c4974d390a044c28aec31e421aa662b2 | MP-fba21121b71148c9bb33e11fcd92d520

### Advertising Intelligence

Product | Production SKU | Demo SKU
--------|----------------|---------
 Express |MP-94072e44d5364872b672d7ab4fc7a7e8 | MP-94072e44d5364872b672d7ab4fc7a7e8

Addon | Production SKU | Demo SKU
--------|----------------|---------
Advanced Reporting | A-3QKQHBS3R6 | A-K73WLF2QL6

 ## Third Party Marketplace Products

The parent product must be already active, or be included in the same order as any of its addons.

Third party products are not activatable on the Demo environment.

Note that Products with Order Forms are not able to be activated via API at this time, and thus are not included in this list. Please Contact support@vendasta.com if there is a Product that you are looking to activate that is not on this list.
 
Product | Addon | Production SKU
--------|-------|---------
Google Ads Robot |  | MP-B77R7GG4QHGKF5RMVSLVZPD3D5JSTZ4K
|  | Google Ads: Monthly Additional Spend | A-SBGZJLT6VD
|  | Google Ads: Additional Spend | A-6MH2JNJKV7
|  | Google Ads: Additional Spend (Large) | A-VGMZ4L2XRF
|  | Google Ads: Monthly Additional Spend (Large) | A-KLJSQ33CGV
|  | White Label | A-KFDWPC5XHZ
Online Appointment Booking |  | MP-0dc18874c6ce47ed96822e08934ba3d9
Form Builder |  | MP-82bc9cad4ce441ef84bc1d7865a9939f
geo.Ad from Chalk Digital |  | MP-MLXNLFH72ZZKHTLDKQ784PCZPJPTPV63
|  | ChalkCredits- One Time | MP-MLXNLFH72ZZKHTLDKQ784PCZPJPTPV63
|  | ChalkCredits - Monthly | MP-MLXNLFH72ZZKHTLDKQ784PCZPJPTPV63
|  | ChalkCredits - Monthly (Higher spend) | MP-MLXNLFH72ZZKHTLDKQ784PCZPJPTPV63
Data-Dynamix Email Marketing |  | MP-2da7414a9f604f518d6ae0127080f9e2
Mobile Marketing & Communications Platform \| Business Texting Dashboard |  | MP-W74STW72G4WVKL52LT45NFJC3XH7M5J2
Mobile Marketing & Communications Platform \| PLUS |  | MP-W74STW72G4WVKL52LT45NFJC3XH7M5J2:EDITION-H3HV6RMH
Mobile Marketing & Communications Platform \| ADVANCED |  | MP-W74STW72G4WVKL52LT45NFJC3XH7M5J2:EDITION-VK8BNJ84
Mobile Marketing & Communications Platform \| PRO |  | MP-W74STW72G4WVKL52LT45NFJC3XH7M5J2:EDITION-4GP72F25
|  | 1,000 Message Credits | A-TXBH4JDMRD
|  | 10,000 Message Credits | A-5ZTN8JGJTN
|  | 2,500 Message Credits | A-DQNTF4XT4R
|  | 25,000 Message Credits | A-XWMRP7LXPL
|  | 5,000 Message Credits | A-CQFNHBHHL4
|  | Digital Kiosk Stand (incl. shipping costs) | A-BGNMVFWGLH
Metricool |  | MP-3QXRWPFX4BTVGZ3NBPSWPS8L3S7QS66W
Pagevamp - Instant Website Builder |  | MP-16d5e227e7b647afaf233595b21bef7a
|  | Pagevamp Website | A-CXZDQ5SQGB
|  | Pagevamp Website Domain | A-5WVMNDWPLS
|  | Unbranded Website | A-MHVQSWJHQ3
Instant Website with Facebook Sync |  | MP-Q7RN4J6FDT2FNLPH6K52SRMCX5QMFS3R
|  | Domain | A-3M3PT2HPB5
|  | Website | A-GKVTPH8FZG
POWr Website Plugins |  | MP-RC8VV8K5DV368F6WJ3ZCSKWG2Q62QSDC
Social Status \| Starter |  | MP-NFWJLTZ2CN55VMRNMHQ46PNSG66M287J:EDITION-WHNMFHKG
Social Status \| Pro |  | MP-NFWJLTZ2CN55VMRNMHQ46PNSG66M287J:EDITION-64QW5D6W
Social Status \| Business |  | MP-NFWJLTZ2CN55VMRNMHQ46PNSG66M287J:EDITION-HDJBV62Q
Social Status \| Company |  | MP-NFWJLTZ2CN55VMRNMHQ46PNSG66M287J:EDITION-HRVWMC5H
Social Status \| Enterprise |  | MP-NFWJLTZ2CN55VMRNMHQ46PNSG66M287J:EDITION-4FSP4PQK
|  | Report Credits | A-6FH4KGDFM4
Visual Visitor |  | MP-ec5edcffc9b74d1b88a1180485d442d5