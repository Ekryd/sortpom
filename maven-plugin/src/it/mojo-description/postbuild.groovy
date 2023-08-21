description = new File(basedir, 'description.txt')
expected_description = new File(basedir, 'expected_description.txt')

assert description.exists()
assert description.text.replaceAll('\r','').contains(expected_description.text.replaceAll('@pom.version@', projectversion).replaceAll('\r',''))

return true
