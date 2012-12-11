--------------------------------------------------------------------------------
"AMBITION" ENGINE
--------------------------------------------------------------------------------

An abstract 2D Java game engine by William 'wilbefast' James Dyce. By default
AWT (Abstract Window Toolkit) is used for input/output. AWT is not appropriate
for most games, but since it comes with the JVM it means that project using it
are a lot easier to distribute.

"Ambition" was originally written for LWJGL, but was ported to AWT to support
ARM processors. Now the main branch uses AWT, with LWJGL as a side project.
I've chosen to do this because some users may not need LWJGL, and so probably
won't want wade through all the dependencies and native libraries to get it
working!

If you need hardware accelerated graphics you can use the lwjgl-ambition fork
of this project.
