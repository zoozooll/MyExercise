# -*- mode: python -*-
a = Analysis(['Clean-up.py'],
             pathex=['F:\\Program Files (x86)\\pyinstaller-2.0'],
             hiddenimports=[],
             hookspath=None)
pyz = PYZ(a.pure)
exe = EXE(pyz,
          a.scripts,
          a.binaries,
          a.zipfiles,
          a.datas,
          name=os.path.join('dist', 'Clean-up.exe'),
          debug=False,
          strip=None,
          upx=True,
          console=True )
