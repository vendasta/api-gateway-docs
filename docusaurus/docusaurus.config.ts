import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';
import type * as OpenApiPlugin from 'docusaurus-plugin-openapi-docs';

const config: Config = {
  title: 'Vendasta Developer Center',
  tagline: 'API documentation and developer guides for the Vendasta platform',
  favicon: 'img/favicon.ico',

  url: 'https://developers.vendasta.com',
  baseUrl: '/',

  organizationName: 'vendasta',
  projectName: 'api-gateway-docs',

  onBrokenLinks: 'warn',
  onBrokenMarkdownLinks: 'warn',

  // Note: rspack bundler disabled — incompatible with postman-code-generators
  // used by docusaurus-theme-openapi-docs (requires Node.js 'path' module)
  // future: {
  //   faster: {
  //     rspackBundler: true,
  //     rspackPersistentCache: true,
  //   },
  // },

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          routeBasePath: '/',
          docItemComponent: '@theme/ApiItem',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  plugins: [
    // Polyfill Node.js 'path' module for postman-code-generators (used by openapi theme)
    function webpackPathPolyfill() {
      return {
        name: 'webpack-path-polyfill',
        configureWebpack() {
          return {
            resolve: {
              fallback: {
                path: require.resolve('path-browserify'),
              },
            },
          };
        },
      };
    },
    [
      'docusaurus-plugin-openapi-docs',
      {
        id: 'api',
        docsPluginId: 'classic',
        config: {
          // Hand-authored YAML specs
          platform: {
            specPath: '../openapi/platform/platform.yaml',
            outputDir: 'docs/api/platform',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          business: {
            specPath: '../openapi/business/business.yaml',
            outputDir: 'docs/api/business',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          advertising: {
            specPath: '../openapi/advertising/advertising.yaml',
            outputDir: 'docs/api/advertising',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          reputation: {
            specPath: '../openapi/reputation/reputation.yaml',
            outputDir: 'docs/api/reputation',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          listings: {
            specPath: '../openapi/listings/listings.yaml',
            outputDir: 'docs/api/listings',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          social: {
            specPath: '../openapi/social/social.yaml',
            outputDir: 'docs/api/social',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          // customervoice: temporarily excluded — spec has operation without summary/operationId
          // that the plugin can't handle. Needs spec fix.
          // customervoice: {
          //   specPath: '../openapi/customervoice/customervoice.yaml',
          //   outputDir: 'docs/api/customervoice',
          //   sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          // } satisfies OpenApiPlugin.Options,
          scim: {
            specPath: '../openapi/scim/scim.yaml',
            outputDir: 'docs/api/scim',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          glossary: {
            specPath: '../openapi/glossary/glossary.yaml',
            outputDir: 'docs/api/glossary',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          api_gateway: {
            specPath: '../openapi/api/api.yaml',
            outputDir: 'docs/api/api-gateway',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          crm: {
            specPath: '../openapi/crm/crm.json',
            outputDir: 'docs/api/crm',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,

          // Auto-generated JSON specs from vendastaapis
          composer: {
            specPath: '../openapi/openapi_external_docs/composer.openapi.json',
            outputDir: 'docs/api/composer',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          conversation: {
            specPath: '../openapi/openapi_external_docs/conversation.openapi.json',
            outputDir: 'docs/api/conversation',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          crm_grpc: {
            specPath: '../openapi/openapi_external_docs/crm.openapi.json',
            outputDir: 'docs/api/crm-grpc',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          forms: {
            specPath: '../openapi/openapi_external_docs/forms.openapi.json',
            outputDir: 'docs/api/forms',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          listing_products: {
            specPath: '../openapi/openapi_external_docs/listing_products.openapi.json',
            outputDir: 'docs/api/listing-products',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          meetings: {
            specPath: '../openapi/openapi_external_docs/meetings.openapi.json',
            outputDir: 'docs/api/meetings',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          multi_location_analytics: {
            specPath: '../openapi/openapi_external_docs/multi_location_analytics.openapi.json',
            outputDir: 'docs/api/multi-location-analytics',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          reputation_grpc: {
            specPath: '../openapi/openapi_external_docs/reputation.openapi.json',
            outputDir: 'docs/api/reputation-grpc',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          sales_orders: {
            specPath: '../openapi/openapi_external_docs/sales_orders.openapi.json',
            outputDir: 'docs/api/sales-orders',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          social_drafts: {
            specPath: '../openapi/openapi_external_docs/social_drafts.openapi.json',
            outputDir: 'docs/api/social-drafts',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          social_posts: {
            specPath: '../openapi/openapi_external_docs/social_posts.openapi.json',
            outputDir: 'docs/api/social-posts',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          vanalytics: {
            specPath: '../openapi/openapi_external_docs/vanalytics.openapi.json',
            outputDir: 'docs/api/vanalytics',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          wordpress_hosting: {
            specPath: '../openapi/openapi_external_docs/wordpress_hosting.openapi.json',
            outputDir: 'docs/api/wordpress-hosting',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          wsp_admin_center: {
            specPath: '../openapi/openapi_external_docs/wsp_admin_center.openapi.json',
            outputDir: 'docs/api/wsp-admin-center',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          wsp_monitor: {
            specPath: '../openapi/openapi_external_docs/wsp_monitor.openapi.json',
            outputDir: 'docs/api/wsp-monitor',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          wsp_site_info: {
            specPath: '../openapi/openapi_external_docs/wsp_site_info.openapi.json',
            outputDir: 'docs/api/wsp-site-info',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          wsp_support_tools: {
            specPath: '../openapi/openapi_external_docs/wsp_support_tools.openapi.json',
            outputDir: 'docs/api/wsp-support-tools',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
          wsp_wp_manager: {
            specPath: '../openapi/openapi_external_docs/wsp_wp_manager.openapi.json',
            outputDir: 'docs/api/wsp-wp-manager',
            sidebarOptions: {groupPathsBy: 'tag', categoryLinkSource: 'tag'},
          } satisfies OpenApiPlugin.Options,
        },
      },
    ],
  ],

  themes: ['docusaurus-theme-openapi-docs'],

  themeConfig: {
    image: 'img/vendasta-social-card.png',
    navbar: {
      title: 'Vendasta Developer Center',
      logo: {
        alt: 'Vendasta Logo',
        src: 'img/vendasta-logo.png',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'guideSidebar',
          position: 'left',
          label: 'Guides',
        },
        {
          type: 'docSidebar',
          sidebarId: 'apiSidebar',
          position: 'left',
          label: 'API Reference',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Documentation',
          items: [
            {
              label: 'Getting Started',
              to: '/',
            },
            {
              label: 'Authorization',
              to: '/authorization',
            },
          ],
        },
        {
          title: 'Resources',
          items: [
            {
              label: 'Vendasta.com',
              href: 'https://www.vendasta.com',
            },
            {
              label: 'Partner Center',
              href: 'https://partners.vendasta.com',
            },
          ],
        },
      ],
      copyright: `Copyright \u00a9 ${new Date().getFullYear()} Vendasta Technologies Inc.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['bash', 'json', 'yaml', 'go', 'java', 'python'],
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
