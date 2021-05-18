import FgenesteService from './fgeneste.service';
import { Component, Inject, Vue } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import AccountService from '@/account/account.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Fgeneste extends Vue {
  @Inject('fgenesteService') private fgenesteService: () => FgenesteService;
  @Inject('accountService') private accountService: () => AccountService;

  private hasAnyAuthorityValue = false;
  private max = 0;
  private value = 0;
  private timer = null;
  private displayBar = false;
  private displayPie = false;
  private displayPie2 = false;
  private displaySpinner = false;
  private myConfig: any = null;
  private myConfig4Void: any = null;
  private datastore: any = null;
  private columns: any = null;

  mounted() {
    this.columns = [
      { field: 'label', key: 'a', title: 'Address', align: 'center' },
      { field: 'val', key: 'b', title: 'Count', align: 'center' },
    ];
    this.myConfig = {
      type: 'pie',
      title: {
        text: 'A Pie Chart',
      },
      plot: {
        tooltip: {
          text: '%t',
          'font-color': 'white',
          'font-family': 'Georgia',
          'font-size': 20,
          'font-weight': 'bold',
          'font-style': 'normal',
        },
        valueBox: {
          visible: false,
          rules: [
            {
              rule: '%v <= 1',
              text: '',
            },
          ],
        },
      },
      series: [
        {
          values: [43],
          text: 'TOTO',
        },
        {
          values: [27],
          text: 'TOTI',
        },
        {
          values: [30],
          text: 'TOTA',
        },
      ],
    };

    this.myConfig4Void = {
      type: 'pie',
      plot: {
        tooltip: {
          text: '%t',
          'font-color': 'white',
          'font-family': 'Georgia',
          'font-size': 20,
          'font-weight': 'bold',
          'font-style': 'normal',
        },
      },
      series: [
        {
          values: [43],
          text: 'TOTO',
        },
        {
          values: [27],
          text: 'TOTI',
        },
        {
          values: [30],
          text: 'TOTA',
        },
      ],
    };
  }

  beforeDestroy() {
    this.stop();
  }

  public viderTable(e) {
    this.fgenesteService()
      .resetMails()
      .then(value => {});
  }

  public stop() {
    clearInterval(this.timer);
    this.timer = null;
    this.value = 0;
    this.max = 0;
    this.displayBar = false;
    this.fgenesteService().stop();
  }

  public recupererMessages() {
    this.displayBar = true;
    this.fgenesteService()
      .getMails()
      .then(value => {});

    this.fgenesteService()
      .getfetchedMails()
      .then(value => {
        this.max = value.data;
      });

    this.timer = setInterval(() => {
      this.fgenesteService()
        .getunfetchedMails()
        .then(value => {
          this.value = value.data;
          console.log(value);
          if (this.value >= this.max) stop();
        });
    }, 2000);
  }

  public onClick(compte) {
    this.displayPie = false;
    this.displayPie2 = false;
    this.displaySpinner = true;
    this.fgenesteService()
      .countByFrom(compte)
      .then(value => {
        let conf: string = JSON.stringify(value.data);
        this.datastore = value.data;
        const label = /label/gi;
        const val = /val/gi;
        conf = conf.replace(label, 'text').replace(val, 'values');
        const doublequote = /""/gi;
        const quote = /"/gi;
        //conf = conf.replace(doublequote,"''").replace(quote,"'");
        //console.log(conf);
        this.myConfig.series = JSON.parse(conf);
        const titre = '"title":{"text":"' + compte + '"}';
        this.myConfig.title.text = compte;
        //console.log(this.myConfig);
        this.displayPie = true;
        this.displaySpinner = false;
      });
    this.fgenesteService()
      .countVoids(compte)
      .then(value => {
        console.log(value);
        const array = value.data.split(',');
        const nonvides = parseInt(array[1]) - parseInt(array[0]);
        const series = '[{"values": [' + array[0] + '],"text": "vide"},{"values": [' + nonvides + '],"text": "non vide"}]';
        console.log(series);
        this.myConfig4Void.series = JSON.parse(series);
        this.displayPie2 = true;
      });
  }

  public hasAnyAuthority(authorities: any): boolean {
    this.accountService()
      .hasAnyAuthorityAndCheckAuth(authorities)
      .then(value => {
        this.hasAnyAuthorityValue = value;
      });
    return this.hasAnyAuthorityValue;
  }

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }
}
