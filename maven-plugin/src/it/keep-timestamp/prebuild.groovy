original = new File(basedir, 'pom.xml')
modified = original.lastModified()

timestampFile = new File(basedir, 'pom-timestamp')
timestampFile.write(modified as String)

return true
