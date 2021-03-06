'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.createReplaceStream = createReplaceStream;

var _stream = require('stream');

function createReplaceStream(toReplace, replacement) {
  if (typeof toReplace !== 'string') {
    throw new TypeError('toReplace must be a string');
  }

  let buffer = Buffer.alloc(0);
  return new _stream.Transform({
    objectMode: false,
    async transform(value, enc, done) {
      try {
        buffer = Buffer.concat([buffer, value], buffer.length + value.length);

        while (true) {
          // try to find the next instance of `toReplace` in buffer
          const index = buffer.indexOf(toReplace);

          // if there is no next instance, break
          if (index === -1) {
            break;
          }

          // flush everything to the left of the next instance
          // of `toReplace`
          this.push(buffer.slice(0, index));

          // then flush an instance of `replacement`
          this.push(replacement);

          // and finally update the buffer to include everything
          // to the right of `toReplace`, dropping to replace from the buffer
          buffer = buffer.slice(index + toReplace.length);
        }

        // until now we have only flushed data that is to the left
        // of a discovered instance of `toReplace`. If `toReplace` is
        // never found this would lead to us buffering the entire stream.
        //
        // Instead, we only keep enough buffer to complete a potentially
        // patial instance of `toReplace`
        if (buffer.length > toReplace.length) {
          // the entire buffer except the last `toReplace.length` bytes
          // so that if all but one byte from `toReplace` is in the buffer,
          // and the next chunk delivers the necessary byte, the buffer will then
          // contain a complete `toReplace` token.
          this.push(buffer.slice(0, buffer.length - toReplace.length));
          buffer = buffer.slice(-toReplace.length);
        }

        done();
      } catch (err) {
        done(err);
      }
    },

    flush(callback) {
      if (buffer.length) {
        this.push(buffer);
      }

      buffer = null;
      callback();
    }
  });
}