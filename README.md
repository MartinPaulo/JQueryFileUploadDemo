# JQueryFileUploadDemo
## About
This is code written to understand how to work with the JQueryFileUpload tool using a Java servlet. 
In particular, the ability to restart a failed upload with having to retransmit the already transmitted parts.
It is at the moment very much a test bed with some rather hacky bits in it.

## Still to be resolved

### Name collisions
Currently an uploaded file will simply replace an existing file of the same name.

### Ordering of parts
The assumption is made that the parts of a multi-part file are all sent in logical order. Is this correct?

### More than one part in a post
The file post uses the servlet getParts method. But then assumes that there's only ever likely to be one part.

### Multiple users/browsers
Multiple users/browsers are open on the same files: hence one user can delete another users files. 
That's OK here, but not what would be wanted in the real world. 
It also means that in a multi-part upload other users can see (and delete) the parts as they are being built up.

### Clean up
If something goes wrong, then no real clean up is done. Hence the upload directory can become littered with parts of files.

### Initialization
No real testing is done of the preconditions required to enable the application to run, hence leaving users to find out 
that something went wrong when they try to upload a file.

### Changes of the file between uploads of large files
If part of a large file is uploaded, and then the upload is interrupted, and then at a later date a newer version of the file
is uploaded, the server will happily add the newer tail to the older head: leaving a file in a rather odd state.

### Cleaning up of a cancel
If a cancel is performed on a large file upload, the server has no idea and leaves the parts on the hard drive.
The user isn't shown the part unless a reload of the page is done.

### Labelling of parts
Currently in a large file the parts are assembled as they are uploaded. It would probably be more efficient to 
save the parts and then join them only once they had all been received... This of course would make the code to work
out how much add already been sent in event of an upload failure more complicated.

### Display of upload parts
If an upload fails the upload part remains in the upload directory with a .part extension. This is deliberately done so the
user can knowingly delete or resume. Not that nice a solution

### Tests
There are no tests. :( 

### And more
Those are just the major oddities that I am aware of. More will come to light.

