[cc-by-nc-sa]: http://creativecommons.org/licenses/by-nc-sa/4.0/
[cc-by-nc-sa-image]: https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png
[cc-by-nc-sa-shield]: https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-lightgrey.svg

## License:

[![CC BY-NC-SA 4.0][cc-by-nc-sa-shield]][cc-by-nc-sa]

This work is licensed under a
[Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License][cc-by-nc-sa].

[![CC BY-NC-SA 4.0][cc-by-nc-sa-image]][cc-by-nc-sa]
<br /><br />
## Credits:

The Module Icons are made by [NGT12-Gaming](https://discord.com/users/416591238704136203).

*The Satistile-Cape are made by [Satistile](https://discord.com/users/629033318817726474).
<br />
The Demon-Eye cosmetics are made by [MrDemonEye aka. Mr Candy](https://discord.com/users/551516410904444939).*

The Client, the Compatiblity Executors, the Loaders and the tools incl. Compiler and Installer and the other cosmetics<br />are made by [MarkusTieger](https://github.com/MarkusTieger).
<br /><br />
## Building:

**Simple method:**<br />
Fork the Repository and use Github Actions (pre-configured)<br />
The Result will be uploaded to the Action.<br /><br />

**CLI Method:**<br />
Clone the Project using `git clone https://github.com/TigerSystems/TigerClient.git`.<br />
In the directory inside the cloned Repository "tools/compiler", run:<br />
`./gradlew -Dgradle.user.home=../../data/gradle run`<br />
The Result is in the directory "data/compiled" inside the Repository.<br /><br />

**Eclipse IDE Method:**<br />
Clone the Repository with [Eclipse IDE for Java Developers](https://www.eclipse.org/downloads/packages/release/2022-09/r/eclipse-ide-java-developers) using EGit (Eclipse Plugin, should be pre-installed) and check the box "Import all existing Eclipse projects after clone finishes"<br />
The Result is in the "*09* Compiled Data" Project.<br /><br />

**Other IDE Method:** *(Not recommended, just use Eclipse IDE)*<br />
Clone the Repository and simple import each gradle project.<br />
The Result is in the directory "data/compiled" inside the Repository.<br /><br />
<br /><br />
## The Vanilla Loader
When you wish to modify the Vanilla Loader, clone the MCP-Reborn repository, run the "setup" task and overwrite the build.gradle and the settings.gradle with the files from the "loader/vanilla" directory inside the Repository. Then you delete the Local Git Repository using `rm -rf .git` and initializes a new one with `git init`. Then you apply the Patch which is in the "loader/vanilla" directory inside the Repository using `git apply \<path to file>`. Now you can edit the Vanilla Loader. When you wish to create a patch from the Loader, you should change the variable "base" in the "build.gradle" in the section for the task "genPatch" to "\<tigerclient git repository path>/loader/vanilla". Now you can run `./gradlew genPatch` and the patch in the Repository will be overwriten.
<br /><br />

## FAQ

**Why i must Patch the Vanilla Loader and not uploading it to Github?**<br />
The Vanilla Loader contains Source-Code from Minecraft. Which i have no permission to share.<br /><br />

**Why does not exist pre-configured IntelliJ or VS Code Support?**<br />
I am too lazy. *( And i hate IntelliJ IDEA )*<br /><br />

**Why should i use the Tigerclient?**<br />
I think it's a great client. It gets active support and new features and actually the question is "Why not?"<br /><br />

<br /><br /><br />
You have unanswered questions? [Open a issue](https://github.com/TigerSystems/TigerClient/issues/new).

<br />

## Open-Source Licenses

<strong>Lua JSE</strong><br/>
License: MIT License<br/>
Project: https://luaj.sourceforge.net/<br/><br/>

<strong>SQLite-JDBC</strong><br/>
License: Apache License 2.0<br/>
Project: https://github.com/xerial/sqlite-jdbc<br/><br/>

<strong>GSON</strong><br/>
License: Apache License 2.0<br/>
Project: https://github.com/google/gson<br/><br/>

<strong>Blossom</strong><br/>
License: GNU LESSER GENERAL PUBLIC LICENSE 2.1<br/>
Project: https://github.com/KyoriPowered/blossom<br/><br/>

<strong>Shadow</strong><br/>
License: Apache License 2.0<br/>
Project: https://github.com/johnrengelman/shadow<br/><br/>

<strong>Forge Gradle</strong><br/>
License: GNU Lesser General Public License v2.1<br/>
Project: https://github.com/MinecraftForge/ForgeGradle<br/><br/>

<strong>Minecraft Forge</strong><br/>
License: GNU LESSER GENERAL PUBLIC LICENSE 2.1<br/>
Project: https://github.com/MinecraftForge/MinecraftForge<br/><br/>

<strong>MCP Reborn</strong><br/>
License: [Custom License](https://github.com/Hexeption/MCP-Reborn/blob/1.19/MCP-License)<br/>
Project: https://github.com/Hexeption/MCP-Reborn/
