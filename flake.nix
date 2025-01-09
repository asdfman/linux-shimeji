{
  description = "Linux Shimeji - Desktop mascot application";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }: 
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        
        defaultShimejis = {
          "little-ghost" = {
            archive = ./hk-imgs/little-ghost.zip;
            license = ./hk-imgs/originallicense.txt;
          };
          "little-ghost-polite" = {
            archive = ./hk-imgs/little-ghost-polite.zip;
            license = ./hk-imgs/licence.txt;
          };
        };
        
        mkShimeji = { variant ? null }: pkgs.stdenv.mkDerivation {
          pname = "linux-shimeji";
          version = "2.2.2";
          
          src = ./.;
          
          nativeBuildInputs = with pkgs; [ 
            ant 
            makeWrapper
            openjdk8-bootstrap
            unzip
          ];
          
          buildInputs = with pkgs; [
            xorg.libX11
            xorg.libXrender
            openjdk8-bootstrap
          ];

          buildPhase = ''
            ant jar
            ${if variant != null then ''
              mkdir -p $out/share/shimeji/img
              mkdir -p shimeji-extract
              cd shimeji-extract
              ${pkgs.unzip}/bin/unzip ${defaultShimejis.${variant}.archive}
              cp -r */ $out/share/shimeji/img/
              cp ${defaultShimejis.${variant}.license} $out/share/shimeji/img/
              cd ..
            '' else ""}
          '';
          
          installPhase = ''
            mkdir -p $out/{bin,lib,share/shimeji}
            cp Shimeji.jar $out/lib/
            cp -r lib $out/lib/
            cp -r conf $out/share/shimeji/
            ${if variant == null then "mkdir -p $out/share/shimeji/img" else ""}
            
            makeWrapper ${pkgs.openjdk8-bootstrap}/bin/java $out/bin/shimeji \
              --add-flags "-classpath $out/lib/Shimeji.jar:$out/lib/lib/*" \
              --add-flags "-Xmx1000m" \
              --add-flags "com.group_finity.mascot.Main" \
              --add-flags "-Djava.util.logging.config.file=$out/share/shimeji/conf/logging.properties" \
              --set CLASSPATH "$out/lib/Shimeji.jar:$out/lib/lib/*" \
              --prefix LD_LIBRARY_PATH : "${pkgs.lib.makeLibraryPath [
                pkgs.xorg.libX11
                pkgs.xorg.libXrender
                pkgs.openjdk8-bootstrap
              ]}" \
              --run "cd $out/share/shimeji"
          '';
          
          meta = with pkgs.lib; {
            description = "This is a Linux version of the popular desktop mascot program, Shimeji";
            longDescription = ''
              This is a Linux version of the popular desktop mascot program, Shimeji.
              Also, this project includes `Little Ghost` images, licenses for them can be found alongside with the compacted images in the source code.
              This project is a fork of the original project by Yuki Yamada.
              This flake is distributed under the MIT license. Portions of the source code included in this flake remain under the zlib license.
            '';
            homepage = "https://github.com/datsfilipe/linux-shimeji";
            license = [ licenses.mit licenses.zlib ];
            maintainers = with maintainers; [ "datsfilipe <datsfilipe.foss@proton.me>" ];
            platforms = platforms.linux;
          };
        };
      in {
        packages = {
          default = mkShimeji {};
          little-ghost = mkShimeji { variant = "little-ghost"; };
          little-ghost-polite = mkShimeji { variant = "little-ghost-polite"; };
        };

        devShells.default = pkgs.mkShell {
          packages = with pkgs; [
            openjdk8-bootstrap
            ant
          ];
          
          LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath [
            pkgs.xorg.libX11
            pkgs.xorg.libXrender
            pkgs.openjdk8-bootstrap
          ];
        };
      }
    );
}
