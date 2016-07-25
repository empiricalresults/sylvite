FROM ruby:2.2

# next node
ENV NODE_VERSION 4.4.7
RUN curl -SLO "http://nodejs.org/dist/v$NODE_VERSION/node-v$NODE_VERSION-linux-x64.tar.gz" \
    && tar -xzf "node-v$NODE_VERSION-linux-x64.tar.gz" -C /usr/local --strip-components=1 \
    && npm install -g npm@"$NPM_VERSION" \
    && npm cache clear

RUN mkdir /site
WORKDIR /site

ADD Gemfile /site
#ADD Gemfile.lock /site

# ruby deps
RUN bundler install

#RUN gem install github-pages:87

