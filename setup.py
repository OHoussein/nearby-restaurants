import os, sys
import shutil
import fnmatch
import time

import re

def findReplace(directory, find, replace, fileRegex):
    for path, dirs, files in os.walk(os.path.abspath(directory)):
        for filename in list(filter(fileRegex.search, files)):
            filepath = os.path.join(path, filename)
            with open(filepath) as f:
                s = f.read()
            s = s.replace(find, replace)
            with open(filepath, "w") as f:
                f.write(s)

namespace  = input("Namespace (com.domain.app): ")
namespacefolders =  namespace.split('.')

# delete the build directory
if os.path.exists("build"):
    shutil.rmtree("build")
if os.path.exists("app/build"):
    shutil.rmtree("app/build")

buidlFlavors = os.listdir("app/src")


for flavor in buidlFlavors:
    basePath = "app/src/" + flavor + "/java"

    level1Path = basePath + "/dev"
    level1PathTarget = basePath + "/" + namespacefolders[0]

    level2Path = level1PathTarget + "/ohoussein"
    level2PathTarget = level1PathTarget + "/" + namespacefolders[1]

    level3Path = level2PathTarget + "/githubtrending"
    level3PathTarget = level2PathTarget + "/" + namespacefolders[2]

    os.rename(level1Path, level1PathTarget)
    time.sleep(.500)
    os.rename(level2Path, level2PathTarget)
    time.sleep(.500)
    os.rename(level3Path, level3PathTarget)
    time.sleep(.500)

findReplace(".", "restos", namespace, re.compile(r'.kt$|.java$|.xml$|.gradle$'))
