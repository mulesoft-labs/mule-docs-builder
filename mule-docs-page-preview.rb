require "formula"

class MuleDocsPagePreview < Formula
  homepage "https://github.com/mulesoft-labs/mule-docs-builder"
  url "https://github.com/mulesoft-labs/mule-docs-builder.git"

  depends_on "maven" => :build
  depends_on :java

  def install
    system "mvn clean package -DskipTests"
    
    libexec.install "mule-docs-single-page-builder/target/mule-docs-single-page-builder-1.0.0-SNAPSHOT.jar"
    
    (bin+"mdocpreview").write <<-EOS.undent
      #!/bin/sh
      java -jar "#{libexec}/mule-docs-single-page-builder-1.0.0-SNAPSHOT.jar" "$@"
    EOS
  end
  
end

