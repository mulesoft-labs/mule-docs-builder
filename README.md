# mule-docs-builder
Tools for building the new MuleSoft documentation site.

## How to install the single page previewer
For Mac OSx users, you will need to have [Homebrew](http://brew.sh/) installed.
To install it, you'll need to type the following command in your Terminal:
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
You'll have to confirm the first request by hitting return, and after that you'll need to insert your password when prompted.

Once the processed is done, it's recommended to type ``` brew doctor ``` to confirm that your installation was successful.
It should return a ```'Your system is ready to brew.' ``` message.

Once Brew is installed, you'll use it to install this repository.
Simply type:
```
brew install http://raw.githubusercontent.com/mulesoft-labs/mule-docs-builder/master/mule-docs-page-preview.rb
```
And follow the installation process (some other packages might be needed in order for ```mule-docs-builder``` to run).

## How to use it.
First, you'll need to define a variable. Name it the same as your .adoc file.
For example, if you have a ```index.adoc``` file, then type:
```
FILENAME=$'index'
```
In your terminal.

After this, simply type:
```
mdocpreview -s /PATH_TO_THE_ADOC_FILE/${FILENAME}.adoc -d /tmp/ ; cd /tmp ; open ${FILENAME}.html
```
The only thing you'll need to replace here would be the ```PATH_TO_THE_ADOC_FILE``` with the actual location of your .adoc.
Leave both ```${FILENAME}``` fields as they are in the example.
