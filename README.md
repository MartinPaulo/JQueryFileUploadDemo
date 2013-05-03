# JQueryFileUploadDemo
## About
This is code written to understand how to work with the JQueryFileUpload tool using a Java servlet. 
In particular, the ability to restart a failed upload with having to retransmit the already transmitted parts.
It is at the moment very much a test bed with some rather hacky bits in it.

## Note:

### Identity
It is assumed that File.lastModifiedDate and File name are, between them, enough to indicate if one file is the same as another file.
Short of writing MD5 checksums that's about the best I can think of. This is really only an issue on a resumed multi-part upload.

### Name collisions
Currently an uploaded file will simply replace an existing file of the same name.

### Ordering of parts
The assumption is made that the parts of a multi-part file are all sent in logical order. Is this correct?

### More than one part in a post
The file post uses the servlet getParts method. But then assumes that there's only ever likely to be one part.

### Clean up
If something goes wrong, with a multipart upload, the server doesn't try to clean up. Hence the temporary directory 
can become littered with parts of files. Also, if a cancel is performed on a large multi-part file upload, the server has no 
idea and leaves the parts on the hard drive.

### Labelling of parts
Currently in a large file the parts are assembled as they are uploaded. It would probably be more efficient to 
save the parts and then join them only once they had all been received... This of course would make the code to work
out how much add already been sent in event of an upload failure more complicated.

## Still to be resolved

### Multiple users/browsers
Multiple users/browsers are open on the same files: hence one user can delete another users files. 
That's OK here, but not what would be wanted in the real world. 

### Initialization
No real testing is done of the preconditions required to enable the application to run, hence leaving users to find out 
that something went wrong when they try to upload a file.

### Tests
There are no tests. :( 

### And more
Those are just the major oddities that I am aware of. More will come to light.

