# Performing a release

This is my cheat sheet for doing a release.

## Prepare

Ensure that `.m2/settings.xml` contains the GitHub credentials:

    <server>
      <id>github</id>
      <password><!-- access key --></password>
    </server>

## Do the release

    mvn release:clean release:prepare release:perform

## Maven Central

Head over to http://oss.sonatype.org/ and do the release

## Update the master branch

    git checkout master
    git merge <tag>
    git checkout develop

## Upload the documentation

    git checkout <tag>
    # prepare and maybe modify site.xml
    mvn site:site site:stage
    # upload
    git checkout develop

